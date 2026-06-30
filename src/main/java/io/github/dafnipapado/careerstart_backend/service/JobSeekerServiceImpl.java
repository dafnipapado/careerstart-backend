package io.github.dafnipapado.careerstart_backend.service;

import io.github.dafnipapado.careerstart_backend.core.exception.EntityAlreadyExistsException;
import io.github.dafnipapado.careerstart_backend.core.exception.EntityNotFoundException;
import io.github.dafnipapado.careerstart_backend.core.exception.FileUploadException;
import io.github.dafnipapado.careerstart_backend.dto.attachment.AttachmentUploadDTO;
import io.github.dafnipapado.careerstart_backend.dto.job_seeker.JobSeekerDetailsReadOnlyDTO;
import io.github.dafnipapado.careerstart_backend.dto.job_seeker.JobSeekerInsertDTO;
import io.github.dafnipapado.careerstart_backend.dto.job_seeker.JobSeekerReadOnlyDTO;
import io.github.dafnipapado.careerstart_backend.dto.job_seeker.JobSeekerUpdateDTO;
import io.github.dafnipapado.careerstart_backend.mapper.Mapper;
import io.github.dafnipapado.careerstart_backend.model.Attachment;
import io.github.dafnipapado.careerstart_backend.model.JobSeeker;
import io.github.dafnipapado.careerstart_backend.model.PersonalInfo;
import io.github.dafnipapado.careerstart_backend.model.static_data.Region;
import io.github.dafnipapado.careerstart_backend.model.static_data.Role;
import io.github.dafnipapado.careerstart_backend.repository.AttachmentRepository;
import io.github.dafnipapado.careerstart_backend.repository.JobSeekerRepository;
import io.github.dafnipapado.careerstart_backend.repository.PersonalInfoRepository;
import io.github.dafnipapado.careerstart_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobSeekerServiceImpl implements IJobSeekerService{

    private final IUserService userService;
    private final IPersonalInfoService personalInfoService;
    private final IAttachmentService attachmentService;
    private final JobSeekerRepository jobSeekerRepository;
    private final UserRepository userRepository;
    private final PersonalInfoRepository personalInfoRepository;
    private final AttachmentRepository attachmentRepository;
    private final Mapper mapper;
    private final PasswordEncoder passwordEncoder;
    private static final String JOB_SEEKER_ROLE_NAME = "JOB_SEEKER";

    @Override
    @Transactional(rollbackFor = {EntityNotFoundException.class, EntityAlreadyExistsException.class})
    public JobSeekerReadOnlyDTO save(JobSeekerInsertDTO jobSeekerInsertDTO) throws EntityNotFoundException, EntityAlreadyExistsException {

        //checks for already existing username and email
        if (userRepository.findByUsername(jobSeekerInsertDTO.userInsertDTO().username()).isPresent()) {
            throw new EntityAlreadyExistsException("User", "User with username = {" + jobSeekerInsertDTO.userInsertDTO().username() + "} already exists.");
        }
        if (personalInfoRepository.findByEmail(jobSeekerInsertDTO.personalInfoInsertDTO().email()).isPresent()) {
            throw new EntityAlreadyExistsException("PersonalInfo", "Personal Info with email = {" + jobSeekerInsertDTO.personalInfoInsertDTO().email() + "} already exists.");
        }

        JobSeeker jobSeeker = mapper.mapToJobSeekerEntity(jobSeekerInsertDTO);

        //set encoded password
        jobSeeker.getUser().setPassword(passwordEncoder.encode(jobSeekerInsertDTO.userInsertDTO().password()));

        //fetch and set related entities
        Role role = userService.getRoleByName(JOB_SEEKER_ROLE_NAME);
        jobSeeker.getUser().setRole(role);

        Long regionId = jobSeekerInsertDTO.personalInfoInsertDTO().regionId();
        Region region = personalInfoService.getRegionById(regionId);
        jobSeeker.getPersonalInfo().setRegion(region);

        //save jobseeker entity
        jobSeekerRepository.save(jobSeeker);
        log.info("Job seeker {{} {}} was saved successfully.", jobSeekerInsertDTO.firstname(), jobSeekerInsertDTO.lastname());

        return mapper.mapToJobSeekerReadOnlyDTO(jobSeeker);
    }

    @Override
    @Transactional(rollbackFor = {EntityNotFoundException.class, EntityAlreadyExistsException.class})
    public JobSeekerReadOnlyDTO update(JobSeekerUpdateDTO jobSeekerUpdateDTO) throws EntityNotFoundException, EntityAlreadyExistsException {

        JobSeeker jobSeeker = getJobSeekerByUuid(jobSeekerUpdateDTO.uuid());

        //set updated fields for jobseeker
        jobSeeker.setFirstname(jobSeekerUpdateDTO.firstname());
        jobSeeker.setLastname(jobSeekerUpdateDTO.lastname());

        //set updated fields for user
        //-check for already existing username, if changed
        String updatedUsername = jobSeekerUpdateDTO.userUpdateDTO().username();
        if (!Objects.equals(updatedUsername, jobSeeker.getUser().getUsername()) && userRepository.findByUsername(updatedUsername).isPresent()) {
            throw new EntityAlreadyExistsException("User", "User with username = {" + updatedUsername + "} already exists.");
        }
        jobSeeker.getUser().setUsername(updatedUsername);

        //set updated fields for personalInfo
        //-check for already existing email, if changed
        String updatedEmail = jobSeekerUpdateDTO.personalInfoUpdateDTO().email();
        if (!Objects.equals(updatedEmail, jobSeeker.getPersonalInfo().getEmail()) && personalInfoRepository.findByEmail(updatedEmail).isPresent()) {
            throw new EntityAlreadyExistsException("PersonalInfo", "Personal Info with email = {" + updatedEmail + "} already exists.");
        }
        jobSeeker.getPersonalInfo().setEmail(updatedEmail);
        jobSeeker.getPersonalInfo().setTelephoneNumber(jobSeekerUpdateDTO.personalInfoUpdateDTO().telephoneNumber());
        jobSeeker.getPersonalInfo().setAddress(jobSeekerUpdateDTO.personalInfoUpdateDTO().address());
        //-find and set the updated region, if changed
        if (!Objects.equals(jobSeekerUpdateDTO.personalInfoUpdateDTO().regionId(), jobSeeker.getPersonalInfo().getRegion().getId())) {
            Region updatedRegion = personalInfoService.getRegionById(jobSeekerUpdateDTO.personalInfoUpdateDTO().regionId());
            jobSeeker.getPersonalInfo().getRegion().removePersonalInfo(jobSeeker.getPersonalInfo());
            updatedRegion.addPersonalInfo(jobSeeker.getPersonalInfo());
        }

        log.info("Job Seeker with uuid = {" + jobSeeker.getUuid() + "} was updated successfully.");

        return mapper.mapToJobSeekerReadOnlyDTO(jobSeeker);
    }

    @Override
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public JobSeekerReadOnlyDTO delete(UUID uuid) throws EntityNotFoundException {

        JobSeeker jobSeeker = getJobSeekerByUuid(uuid);
        jobSeeker.softDelete();
        jobSeeker.getUser().softDelete();
        jobSeeker.getPersonalInfo().softDelete();

        log.info("Job Seeker with uuid = {" + uuid + "} was soft deleted successfully.");

        return mapper.mapToJobSeekerReadOnlyDTO(jobSeeker);
    }

    @Override
    public JobSeekerDetailsReadOnlyDTO getSingleJobSeeker(UUID uuid) throws EntityNotFoundException {
        JobSeeker jobSeeker = getJobSeekerByUuid(uuid);

        log.info("Job seeker with uuid = {" + uuid + "} was fetched successfully.");
        return mapper.mapToJobSeekerDetailsReadOnlyDTO(jobSeeker);
    }

    @Override
    public JobSeekerDetailsReadOnlyDTO getSingleJobSeekerDeletedFalse(UUID uuid) throws EntityNotFoundException {
        JobSeeker jobSeeker = getJobSeekerByUuidDeletedFalse(uuid);

        log.info("Active job seeker with uuid = {" + uuid + "} was fetched successfully.");
        return mapper.mapToJobSeekerDetailsReadOnlyDTO(jobSeeker);
    }

    @Override
    @Retryable(
        retryFor = {IOException.class, HttpServerErrorException.class},
        backoff = @Backoff(delay = 2000L, multiplier = 2, maxDelay = 10000)
    )
    @Transactional(rollbackFor = {EntityNotFoundException.class, FileUploadException.class})
    public void uploadDocument(UUID uuid, MultipartFile file) throws EntityNotFoundException, FileUploadException {
        try {
            JobSeeker jobSeeker = getJobSeekerByUuid(uuid);
            PersonalInfo personalInfo = jobSeeker.getPersonalInfo();

            AttachmentUploadDTO attachmentUploadDTO = attachmentService.uploadAttachment(uuid, file, "jobseeker");

            if (attachmentUploadDTO.existingFilePath() != null) {
                Attachment attachment = attachmentRepository.findByFilepath(attachmentUploadDTO.existingFilePath().toString())
                        .orElseThrow(() -> new EntityNotFoundException("Attachment", "Existing attachment in filepath = {"
                                + attachmentUploadDTO.existingFilePath().toString() + "} for job seeker with uuid = {" + uuid + "} not found."));
                personalInfo.removeAttachment(attachment);
                attachmentRepository.delete(attachment);
            }

            Attachment attachment = mapper.mapToAttachmentEntity(attachmentUploadDTO);

            personalInfo.addAttachment(attachment);
            attachmentRepository.save(attachment);

            log.info("Attachment for job seeker with uuid = {" + uuid + "} was uploaded successfully.");
        } catch (IOException e) {
            throw new FileUploadException("JobSeekerAttachment", "Attachment upload for job seeker with uuid = {" + uuid + "} failed.", e);
        }
    }

    @Override
    public JobSeeker getJobSeekerByUuid(UUID uuid) throws EntityNotFoundException {
        return jobSeekerRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("JobSeeker", "Job Seeker with uuid = {" + uuid + "} not found."));
    }

    @Override
    public JobSeeker getJobSeekerByUuidDeletedFalse(UUID uuid) throws EntityNotFoundException {
        return jobSeekerRepository.findByUuidAndDeletedFalse(uuid)
                .orElseThrow(() -> new EntityNotFoundException("JobSeeker", "Active job seeker with uuid = {" + uuid + "} not found."));
    }

}
