package io.github.dafnipapado.careerstart_backend.service;

import io.github.dafnipapado.careerstart_backend.core.exception.EntityAlreadyExistsException;
import io.github.dafnipapado.careerstart_backend.core.exception.EntityNotFoundException;
import io.github.dafnipapado.careerstart_backend.core.exception.FileReadException;
import io.github.dafnipapado.careerstart_backend.core.exception.FileUploadException;
import io.github.dafnipapado.careerstart_backend.dto.attachment.AttachmentReadDTO;
import io.github.dafnipapado.careerstart_backend.dto.attachment.AttachmentUploadDTO;
import io.github.dafnipapado.careerstart_backend.dto.employer.*;
import io.github.dafnipapado.careerstart_backend.filters.EmployerFilters;
import io.github.dafnipapado.careerstart_backend.mapper.Mapper;
import io.github.dafnipapado.careerstart_backend.model.Attachment;
import io.github.dafnipapado.careerstart_backend.model.Employer;
import io.github.dafnipapado.careerstart_backend.model.PersonalInfo;
import io.github.dafnipapado.careerstart_backend.model.User;
import io.github.dafnipapado.careerstart_backend.model.static_data.ProfessionalField;
import io.github.dafnipapado.careerstart_backend.model.static_data.Region;
import io.github.dafnipapado.careerstart_backend.model.static_data.Role;
import io.github.dafnipapado.careerstart_backend.repository.*;
import io.github.dafnipapado.careerstart_backend.specification.EmployerSpecification;
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
public class EmployerServiceImpl implements IEmployerService{

    private final Mapper mapper;
    private final ProfessionalFieldRepository professionalFieldRepository;
    private final EmployerRepository employerRepository;
    private final UserRepository userRepository;
    private final PersonalInfoRepository personalInfoRepository;
    private final AttachmentRepository attachmentRepository;
    private final JobListingRepository jobListingRepository;
    private final IUserService userService;
    private final IRegionService regionService;
    private final IAttachmentService attachmentService;
    private final PasswordEncoder passwordEncoder;
    private final static String EMPLOYER_ROLE_NAME = "EMPLOYER";

    @Override
    @Transactional(rollbackFor = {EntityNotFoundException.class, EntityAlreadyExistsException.class})
    public EmployerReadOnlyDTO save(EmployerInsertDTO employerInsertDTO) throws EntityNotFoundException, EntityAlreadyExistsException {

        //checks for already existing vat, username and email
        if (employerRepository.findByVat(employerInsertDTO.vat()).isPresent()) {
            throw new EntityAlreadyExistsException("EmployerVat", "Employer with vat = {" + employerInsertDTO.vat() + "} already exists.");
        }
        if (userRepository.findByUsername(employerInsertDTO.userInsertDTO().username()).isPresent()) {
            throw new EntityAlreadyExistsException("UserUsername", "User with username = {" + employerInsertDTO.userInsertDTO().username() + "} already exists.");
        }
        if (personalInfoRepository.findByEmail(employerInsertDTO.personalInfoInsertDTO().email()).isPresent()) {
            throw new EntityAlreadyExistsException("UserEmail", "User with email = {" + employerInsertDTO.personalInfoInsertDTO().email() + "} already exists.");
        }

        Employer employer = mapper.mapToEmployerEntity(employerInsertDTO);

        //encode password
        employer.getUser().setPassword(passwordEncoder.encode(employerInsertDTO.userInsertDTO().password()));

        //fetch and set related entities
        Long professionalFieldId = employerInsertDTO.professionalFieldId();
        ProfessionalField professionalField = getProfessionalFieldById(professionalFieldId);
        employer.setProfessionalField(professionalField);

        Role role = userService.getRoleByName(EMPLOYER_ROLE_NAME);
        employer.getUser().setRole(role);

        Long regionId = employerInsertDTO.personalInfoInsertDTO().regionId();
        Region region = regionService.getRegionById(regionId);
        employer.getPersonalInfo().setRegion(region);

        //save employer entity
        employerRepository.save(employer);
        log.info("Employer {" + employerInsertDTO.brandName() + "} was saved successfully.");

        return mapper.mapToEmployerReadOnlyDTO(employer);
    }

    @Override
    @Transactional(rollbackFor = {EntityNotFoundException.class, EntityAlreadyExistsException.class})
    public EmployerReadOnlyDTO update(EmployerUpdateDTO employerUpdateDTO) throws EntityNotFoundException, EntityAlreadyExistsException {

        Employer employer = getEmployerByUuid(employerUpdateDTO.uuid());

        //set updated fields for employer
        employer.setBrandName(employerUpdateDTO.brandName());
        employer.setWebsite(employerUpdateDTO.website());
        employer.setProfile(employerUpdateDTO.profile());
        //-check for already existing vat, if changed
        String updatedVat = employerUpdateDTO.vat();
        if (!Objects.equals(updatedVat, employer.getVat()) && employerRepository.findByVat(updatedVat).isPresent()) {
            throw new EntityAlreadyExistsException("EmployerVat", "Employer with vat = {" + updatedVat + "} already exists.");
        }
        employer.setVat(updatedVat);
        //-find and set the updated professionalId, if changed
        if (!Objects.equals(employerUpdateDTO.professionalFieldId(), employer.getProfessionalField().getId())) {
            ProfessionalField updatedProfessionalField = getProfessionalFieldById(employerUpdateDTO.professionalFieldId());
            employer.getProfessionalField().removeEmployer(employer);
            updatedProfessionalField.addEmployer(employer);
        }

        //set updated fields for user
        //-check for already existing username, if changed
        String updatedUsername = employerUpdateDTO.userUpdateDTO().username();
        if (!Objects.equals(updatedUsername, employer.getUser().getUsername()) && userRepository.findByUsername(updatedUsername).isPresent()) {
            throw new EntityAlreadyExistsException("UserUsername", "User with username = {" + updatedUsername + "} already exists.");
        }
        employer.getUser().setUsername(updatedUsername);

        //set updated fields for personalInfo
        //-check for already existing email, if changed
        String updatedEmail = employerUpdateDTO.personalInfoUpdateDTO().email();
        if (!Objects.equals(updatedEmail, employer.getPersonalInfo().getEmail()) && personalInfoRepository.findByEmail(updatedEmail).isPresent()) {
            throw new EntityAlreadyExistsException("UserEmail", "User with email = {" + updatedEmail + "} already exists.");
        }
        employer.getPersonalInfo().setEmail(updatedEmail);
        employer.getPersonalInfo().setTelephoneNumber(employerUpdateDTO.personalInfoUpdateDTO().telephoneNumber());
        employer.getPersonalInfo().setAddress(employerUpdateDTO.personalInfoUpdateDTO().address());
        //-find and set the updated region, if changed
        if(!Objects.equals(employerUpdateDTO.personalInfoUpdateDTO().regionId(), employer.getPersonalInfo().getRegion().getId())) {
            Region updatedRegion = regionService.getRegionById(employerUpdateDTO.personalInfoUpdateDTO().regionId());
            employer.getPersonalInfo().getRegion().removePersonalInfo(employer.getPersonalInfo());
            updatedRegion.addPersonalInfo(employer.getPersonalInfo());
        }

        log.info("Employer with uuid = {" + employer.getUuid() + "} was updated successfully.");

        return mapper.mapToEmployerReadOnlyDTO(employer);
    }

    @Override
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public EmployerReadOnlyDTO delete(UUID uuid) throws EntityNotFoundException {

        Employer employer = getEmployerByUuidDeletedFalse(uuid);
        employer.softDelete();
        employer.getUser().softDelete();
        employer.getPersonalInfo().softDelete();

        log.info("Employer with uuid = {" + uuid + "} was soft deleted successfully");

        return mapper.mapToEmployerReadOnlyDTO(employer);
    }

    @Override
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public void activate(UUID uuid) throws EntityNotFoundException {
        Employer employer = employerRepository.findByUuidAndDeletedTrue(uuid)
                        .orElseThrow(() -> new EntityNotFoundException("Employer", "No deactivated employer with uuid = {" + uuid + "} found"));

        employer.activate();
        employer.getPersonalInfo().activate();
        employer.getUser().activate();
        log.info("Employer with uuid = {" + uuid + "} was activated successfully");
    }

    @Override
    public EmployerDetailsReadOnlyDTO getSingleEmployer(UUID uuid) throws EntityNotFoundException {
        Employer employer = getEmployerByUuid(uuid);

        log.info("Employer with uuid = {" + uuid + "} was fetched successfully.");
        return mapper.mapToEmployerDetailsReadOnlyDTO(employer);
    }

    @Override
    public EmployerDetailsReadOnlyDTO getSingleEmployerDeletedFalse(UUID uuid) throws EntityNotFoundException {
        Employer employer = getEmployerByUuidDeletedFalse(uuid);

        log.info("Active employer with uuid = {" + uuid + "} was fetched successfully.");
        return mapper.mapToEmployerDetailsReadOnlyDTO(employer);
    }

    @Override
    @Retryable(
        retryFor = {FileUploadException.class},
        backoff = @Backoff(delay = 2000L, multiplier = 2, maxDelay = 10000)
    )
    @Transactional(rollbackFor = {EntityNotFoundException.class, FileUploadException.class})
    public void uploadAttachment(UUID uuid, MultipartFile file) throws EntityNotFoundException, FileUploadException {
        Employer employer = userService.getCurrentUser().getEmployer();
        PersonalInfo personalInfo = employer.getPersonalInfo();

        AttachmentUploadDTO attachmentUploadDTO = attachmentService.uploadAttachment(uuid, file, "employer", "picture");

        //remove previous attachment from personalInfo's set and delete from database, in case it existed
        if (attachmentUploadDTO.existingFilePath() != null) {
            Attachment existingAttachment = attachmentRepository.findByFilepath(attachmentUploadDTO.existingFilePath().toString())
                    .orElseThrow(() -> new EntityNotFoundException("AttachmentPicture", "Existing attachment for employer with uuid = {" + uuid + "} not found."));
            personalInfo.removeAttachment(existingAttachment);
            attachmentRepository.delete(existingAttachment);
        }

        Attachment attachment = mapper.mapToAttachmentEntity(attachmentUploadDTO);

        personalInfo.addAttachment(attachment);
        attachmentRepository.save(attachment);

        log.info("Attachment for employer with uuid = {" + uuid + "} was uploaded successfully.");
    }

    @Override
    public AttachmentReadDTO getProfilePicture(UUID employerUuid) throws EntityNotFoundException, FileReadException {
        Attachment attachment = getEmployerByUuidDeletedFalse(employerUuid).getPersonalInfo().getAttachments().stream().findFirst()
                .orElseThrow(() -> new EntityNotFoundException("AttachmentPicture", "Employer with uuid = {" + employerUuid + "} hasn't uploaded a profile picture"));

        return attachmentService.getAttachmentData(attachment);
    }

    @Override
    public Page<EmployerDetailsReadOnlyDTO> getPaginatedFilteredEmployers(EmployerFilters employerFilters) throws EntityNotFoundException {
        if (employerFilters.getUuid() != null) {
            Employer employer = getEmployerByUuid(employerFilters.getUuid());
            return getSingleResultPage(employerFilters.getPageable(), employer);
        }
        if (employerFilters.getVat() != null) {
            Employer employer = employerRepository.findByVat(employerFilters.getVat())
                    .orElseThrow(() -> new EntityNotFoundException("EmployerVat", "Employer with vat = {" + employerFilters.getVat() + "} not found."));
            return getSingleResultPage(employerFilters.getPageable(), employer);
        }

        var filtered = employerRepository.findAll(EmployerSpecification.build(employerFilters), employerFilters.getPageable());

        log.info("Filtered {} employers.", filtered.getNumberOfElements());
        return filtered.map(mapper::mapToEmployerDetailsReadOnlyDTO);
    }

    private Page<EmployerDetailsReadOnlyDTO> getSingleResultPage(Pageable pageable, Employer employer) {
        return new PageImpl<>(
                List.of(mapper.mapToEmployerDetailsReadOnlyDTO(employer)),
                pageable,
                1
        );
    }

    @Override
    public EmployerDetailsReadOnlyDTO getCurrentEmployer() {
        User currentUser = userService.getCurrentUser();
        Employer currentEmployer = currentUser.getEmployer();
        return mapper.mapToEmployerDetailsReadOnlyDTO(currentEmployer);
    }

    @Override
    public Employer getEmployerByUuid(UUID uuid) throws EntityNotFoundException {
        return employerRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Employer", "Employer with uuid = {" + uuid + "} not found."));
    }

    @Override
    public Employer getEmployerByUuidDeletedFalse(UUID uuid) throws EntityNotFoundException {
        return employerRepository.findByUuidAndDeletedFalse(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Employer", "Active employer with uuid = {" + uuid + "} not found."));
    }

    @Override
    public ProfessionalField getProfessionalFieldById(Long professionalFieldId) throws EntityNotFoundException {
        return professionalFieldRepository.findById(professionalFieldId)
                .orElseThrow(() -> new EntityNotFoundException("ProfessionalField", "Professional field with id = {" + professionalFieldId + "} not found."));
    }

    @Override
    public long countEmployerJobListings() {
        Employer employer = userService.getCurrentUser().getEmployer();
        return jobListingRepository.countByEmployerUuidAndDeletedFalse(employer.getUuid());
    }

    @Override
    public long countEmployerJobListingsByEmployerUuid(UUID uuid) {
        return jobListingRepository.countByEmployerUuidAndDeletedFalse(uuid);
    }

}
