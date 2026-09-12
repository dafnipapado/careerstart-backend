package io.github.dafnipapado.careerstart_backend.service;

import io.github.dafnipapado.careerstart_backend.core.exception.EntityAlreadyExistsException;
import io.github.dafnipapado.careerstart_backend.core.exception.EntityNotFoundException;
import io.github.dafnipapado.careerstart_backend.core.exception.FileReadException;
import io.github.dafnipapado.careerstart_backend.core.exception.FileUploadException;
import io.github.dafnipapado.careerstart_backend.dto.attachment.AttachmentReadDTO;
import io.github.dafnipapado.careerstart_backend.dto.attachment.AttachmentUploadDTO;
import io.github.dafnipapado.careerstart_backend.dto.job_seeker.*;
import io.github.dafnipapado.careerstart_backend.filters.JobSeekerFilters;
import io.github.dafnipapado.careerstart_backend.mapper.Mapper;
import io.github.dafnipapado.careerstart_backend.model.*;
import io.github.dafnipapado.careerstart_backend.model.static_data.Region;
import io.github.dafnipapado.careerstart_backend.model.static_data.Role;
import io.github.dafnipapado.careerstart_backend.repository.AttachmentRepository;
import io.github.dafnipapado.careerstart_backend.repository.JobSeekerRepository;
import io.github.dafnipapado.careerstart_backend.repository.PersonalInfoRepository;
import io.github.dafnipapado.careerstart_backend.repository.UserRepository;
import io.github.dafnipapado.careerstart_backend.specification.JobSeekerSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobSeekerServiceImpl implements IJobSeekerService{

    private final IUserService userService;
    private final IRegionService regionService;
    private final IAttachmentService attachmentService;
    private final IJobListingService jobListingService;
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
            throw new EntityAlreadyExistsException("UserUsername", "User with username = {" + jobSeekerInsertDTO.userInsertDTO().username() + "} already exists.");
        }
        if (personalInfoRepository.findByEmail(jobSeekerInsertDTO.personalInfoInsertDTO().email()).isPresent()) {
            throw new EntityAlreadyExistsException("UserEmail", "User with email = {" + jobSeekerInsertDTO.personalInfoInsertDTO().email() + "} already exists.");
        }

        JobSeeker jobSeeker = mapper.mapToJobSeekerEntity(jobSeekerInsertDTO);

        //set encoded password
        jobSeeker.getUser().setPassword(passwordEncoder.encode(jobSeekerInsertDTO.userInsertDTO().password()));

        //fetch and set related entities
        Role role = userService.getRoleByName(JOB_SEEKER_ROLE_NAME);
        jobSeeker.getUser().setRole(role);

        Long regionId = jobSeekerInsertDTO.personalInfoInsertDTO().regionId();
        Region region = regionService.getRegionById(regionId);
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
            throw new EntityAlreadyExistsException("UserUsername", "User with username = {" + updatedUsername + "} already exists.");
        }
        jobSeeker.getUser().setUsername(updatedUsername);

        //set updated fields for personalInfo
        //-check for already existing email, if changed
        String updatedEmail = jobSeekerUpdateDTO.personalInfoUpdateDTO().email();
        if (!Objects.equals(updatedEmail, jobSeeker.getPersonalInfo().getEmail()) && personalInfoRepository.findByEmail(updatedEmail).isPresent()) {
            throw new EntityAlreadyExistsException("UserEmail", "User with email = {" + updatedEmail + "} already exists.");
        }
        jobSeeker.getPersonalInfo().setEmail(updatedEmail);
        jobSeeker.getPersonalInfo().setTelephoneNumber(jobSeekerUpdateDTO.personalInfoUpdateDTO().telephoneNumber());
        jobSeeker.getPersonalInfo().setAddress(jobSeekerUpdateDTO.personalInfoUpdateDTO().address());
        //-find and set the updated region, if changed
        if (!Objects.equals(jobSeekerUpdateDTO.personalInfoUpdateDTO().regionId(), jobSeeker.getPersonalInfo().getRegion().getId())) {
            Region updatedRegion = regionService.getRegionById(jobSeekerUpdateDTO.personalInfoUpdateDTO().regionId());
            jobSeeker.getPersonalInfo().getRegion().removePersonalInfo(jobSeeker.getPersonalInfo());
            updatedRegion.addPersonalInfo(jobSeeker.getPersonalInfo());
        }

        log.info("Job Seeker with uuid = {" + jobSeeker.getUuid() + "} was updated successfully.");

        return mapper.mapToJobSeekerReadOnlyDTO(jobSeeker);
    }

    @Override
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public JobSeekerReadOnlyDTO delete(UUID uuid) throws EntityNotFoundException {

        JobSeeker jobSeeker = getJobSeekerByUuidDeletedFalse(uuid);
        jobSeeker.softDelete();
        jobSeeker.getUser().softDelete();
        jobSeeker.getPersonalInfo().softDelete();

        log.info("Job Seeker with uuid = {" + uuid + "} was soft deleted successfully.");

        return mapper.mapToJobSeekerReadOnlyDTO(jobSeeker);
    }

    @Override
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public void activate(UUID uuid) throws EntityNotFoundException {
        JobSeeker jobSeeker = jobSeekerRepository.findByUuidAndDeletedTrue(uuid)
                .orElseThrow(() -> new EntityNotFoundException("JobSeeker", "No deactivated job seeker with uuid = {" + uuid + "} found"));
        jobSeeker.activate();
        jobSeeker.getPersonalInfo().activate();
        jobSeeker.getUser().activate();
        log.info("Job seeker with uuid = {" + uuid + "} was activated successfully");
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
        retryFor = {FileUploadException.class},
        backoff = @Backoff(delay = 2000L, multiplier = 2, maxDelay = 10000)
    )
    @Transactional(rollbackFor = {EntityNotFoundException.class, FileUploadException.class})
    public void uploadAttachment(UUID uuid, MultipartFile file) throws EntityNotFoundException, FileUploadException {

        JobSeeker jobSeeker = userService.getCurrentUserByUuid().getJobSeeker();
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
    }

    @Override
    public AttachmentReadDTO getProfilePicture(UUID jobSeekerUuid) throws EntityNotFoundException, FileReadException {
        Attachment attachment = getJobSeekerByUuidDeletedFalse(jobSeekerUuid).getPersonalInfo().getAttachments().stream()
                .filter(a -> a.getContentType().startsWith("image"))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("AttachmentPicture", "Job seeker with uuid = {" + jobSeekerUuid + "} hasn't uploaded a profile picture"));
        return attachmentService.getAttachmentData(attachment);
    }

    @Override
    public AttachmentReadDTO getCv(UUID jobSeekerUuid) throws EntityNotFoundException, FileReadException {
        Attachment attachment = getJobSeekerByUuidDeletedFalse(jobSeekerUuid).getPersonalInfo().getAttachments().stream()
                .filter(a -> !a.getContentType().startsWith("image"))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("AttachmentCV", "Job seeker with uuid = {" + jobSeekerUuid + "} hasn't uploaded a CV"));
        return attachmentService.getAttachmentData(attachment);
    }

    @Override
    public Page<JobSeekerDetailsReadOnlyDTO> getPaginatedFilteredJobSeekers(JobSeekerFilters jobSeekerFilters) throws EntityNotFoundException {
        if (jobSeekerFilters.getUuid() != null) {
            JobSeeker jobSeeker = getJobSeekerByUuid(jobSeekerFilters.getUuid());
            return getSingleResultPage(jobSeekerFilters.getPageable(), jobSeeker);
        }

        var filtered = jobSeekerRepository.findAll(JobSeekerSpecification.build(jobSeekerFilters), jobSeekerFilters.getPageable());

        log.info("Filtered {} job seekers.", filtered.getNumberOfElements());
        return filtered.map(mapper::mapToJobSeekerDetailsReadOnlyDTO);
    }

    @Override
    public JobSeekerDetailsReadOnlyDTO getCurrentJobSeeker() {
        User currentUser = userService.getCurrentUser();
        JobSeeker currentJobSeeker = currentUser.getJobSeeker();
        return mapper.mapToJobSeekerDetailsReadOnlyDTO(currentJobSeeker);
    }

    @Override
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public void apply(UUID jobListingUuid) throws EntityNotFoundException, EntityAlreadyExistsException {
        if (hasJobListing(jobListingUuid)) throw new EntityAlreadyExistsException("JobSeekerJobListingApply", "Job Seeker has already applied to job listing with uuid = {{jobListingUuid}}");
        JobListing jobListing = jobListingService.getJobListingByUuid(jobListingUuid);
        userService.getCurrentUserByUuid().getJobSeeker().addJobListing(jobListing);
    }

    @Override
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public void withdraw(UUID jobListingUuid) throws EntityNotFoundException, EntityAlreadyExistsException {
        if (!hasJobListing(jobListingUuid)) throw new EntityAlreadyExistsException("JobSeekerJobListingWithdraw", "Job Seeker has already withdrawn from job listing with uuid = {{jobListingUuid}}");
        JobListing jobListing = jobListingService.getJobListingByUuid(jobListingUuid);
        userService.getCurrentUserByUuid().getJobSeeker().removeJobListing(jobListing);
    }

    @Override
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public boolean hasJobListing(UUID jobListingUuid) throws EntityNotFoundException {
        JobListing jobListing = jobListingService.getJobListingByUuidDeletedFalse(jobListingUuid);
        return userService.getCurrentUserByUuid().getJobSeeker().getJobListings().contains(jobListing);
    }

    @Override
    public List<JobSeekerSummaryReadOnlyDTO> getJobSeekersByJobListing(UUID jobListingUuid) throws EntityNotFoundException {
        JobListing jobListing = jobListingService.getJobListingByUuidDeletedFalse(jobListingUuid);
        return jobListing.getJobSeekers()
                .stream()
                .map(mapper::mapToJobSeekerSummaryReadOnlyDTO)
                .toList();
    }

    private Page<JobSeekerDetailsReadOnlyDTO> getSingleResultPage(Pageable pageable, JobSeeker jobSeeker) {
        return new PageImpl<>(
                List.of(mapper.mapToJobSeekerDetailsReadOnlyDTO(jobSeeker)),
                pageable,
                1
        );
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
