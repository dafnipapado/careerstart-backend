package io.github.dafnipapado.careerstart_backend.service;

import io.github.dafnipapado.careerstart_backend.core.exception.EntityAlreadyExistsException;
import io.github.dafnipapado.careerstart_backend.core.exception.EntityNotFoundException;
import io.github.dafnipapado.careerstart_backend.dto.employer.EmployerDetailsReadOnlyDTO;
import io.github.dafnipapado.careerstart_backend.dto.employer.EmployerInsertDTO;
import io.github.dafnipapado.careerstart_backend.dto.employer.EmployerReadOnlyDTO;
import io.github.dafnipapado.careerstart_backend.dto.employer.EmployerUpdateDTO;
import io.github.dafnipapado.careerstart_backend.mapper.Mapper;
import io.github.dafnipapado.careerstart_backend.model.Employer;
import io.github.dafnipapado.careerstart_backend.model.static_data.ProfessionalField;
import io.github.dafnipapado.careerstart_backend.model.static_data.Region;
import io.github.dafnipapado.careerstart_backend.model.static_data.Role;
import io.github.dafnipapado.careerstart_backend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final IUserService userService;
    private final IPersonalInfoService personalInfoService;
    private final PasswordEncoder passwordEncoder;
    private final static Long employerRoleId = 2L;

    @Override
    @Transactional(rollbackFor = {EntityNotFoundException.class, EntityAlreadyExistsException.class})
    public EmployerReadOnlyDTO save(EmployerInsertDTO employerInsertDTO) throws EntityNotFoundException, EntityAlreadyExistsException {

        //checks for already existing vat, username and email
        if (employerRepository.findByVat(employerInsertDTO.vat()).isPresent()) {
            throw new EntityAlreadyExistsException("Employer", "Employer with vat = {" + employerInsertDTO.vat() + "} already exists.");
        }
        if (userRepository.findByUsername(employerInsertDTO.userInsertDTO().username()).isPresent()) {
            throw new EntityAlreadyExistsException("User", "User with username = {" + employerInsertDTO.userInsertDTO().username() + "} already exists.");
        }
        if (personalInfoRepository.findByEmail(employerInsertDTO.personalInfoInsertDTO().email()).isPresent()) {
            throw new EntityAlreadyExistsException("PersonalInfo", "Personal Info with email = {" + employerInsertDTO.personalInfoInsertDTO().email() + "} already exists.");
        }

        Employer employer = mapper.mapToEmployerEntity(employerInsertDTO);

        //encode password
        employer.getUser().setPassword(passwordEncoder.encode(employerInsertDTO.userInsertDTO().password()));

        //fetch and set related entities
        Long professionalFieldId = employerInsertDTO.professionalFieldId();
        ProfessionalField professionalField = getProfessionalFieldById(professionalFieldId);
        employer.setProfessionalField(professionalField);

        Role role = userService.getRoleById(employerRoleId);
        employer.getUser().setRole(role);

        Long regionId = employerInsertDTO.personalInfoInsertDTO().regionId();
        Region region = personalInfoService.getRegionById(regionId);
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
        //-check for already existing vat, if changed
        String updatedVat = employerUpdateDTO.vat();
        if (!Objects.equals(updatedVat, employer.getVat()) && employerRepository.findByVat(updatedVat).isPresent()) {
            throw new EntityAlreadyExistsException("Employer", "Employer with vat = {" + updatedVat + "} already exists.");
        }
        employer.setVat(updatedVat);
        //-find and set the updated professionalId, if changed
        if (!Objects.equals(employerUpdateDTO.professionalFieldId(), employer.getProfessionalField().getId())) {
            ProfessionalField updatedProfessionalField = getProfessionalFieldById(employerUpdateDTO.professionalFieldId());
            employer.getProfessionalField().remove(employer);
            updatedProfessionalField.addEmployer(employer);
        }

        //set updated fields for user
        //-check for already existing username, if changed
        String updatedUsername = employerUpdateDTO.userUpdateDTO().username();
        if (!Objects.equals(updatedUsername, employer.getUser().getUsername()) && userRepository.findByUsername(updatedUsername).isPresent()) {
            throw new EntityAlreadyExistsException("User", "User with username = {" + updatedUsername + "} already exists.");
        }
        employer.getUser().setUsername(updatedUsername);

        //set updated fields for personalInfo
        //-check for already existing email, if changed
        String updatedEmail = employerUpdateDTO.personalInfoUpdateDTO().email();
        if (!Objects.equals(updatedEmail, employer.getPersonalInfo().getEmail()) && personalInfoRepository.findByEmail(updatedEmail).isPresent()) {
            throw new EntityAlreadyExistsException("PersonalInfo", "Personal Info with email = {" + updatedEmail + "} already exists.");
        }
        employer.getPersonalInfo().setEmail(updatedEmail);
        employer.getPersonalInfo().setTelephoneNumber(employerUpdateDTO.personalInfoUpdateDTO().telephoneNumber());
        employer.getPersonalInfo().setAddress(employerUpdateDTO.personalInfoUpdateDTO().address());
        //-find and set the updated region, if changed
        if(!Objects.equals(employerUpdateDTO.personalInfoUpdateDTO().regionId(), employer.getPersonalInfo().getRegion().getId())) {
            Region updatedRegion = personalInfoService.getRegionById(employerUpdateDTO.personalInfoUpdateDTO().regionId());
            employer.getPersonalInfo().getRegion().removePersonalInfo(employer.getPersonalInfo());
            updatedRegion.addPersonalInfo(employer.getPersonalInfo());
        }

        log.info("Employer with uuid = {" + employer.getUuid() + "} was updated successfully.");

        return mapper.mapToEmployerReadOnlyDTO(employer);
    }

    @Override
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public EmployerReadOnlyDTO delete(UUID uuid) throws EntityNotFoundException {

        Employer employer = getEmployerByUuid(uuid);
        employer.softDelete();
        employer.getUser().softDelete();
        employer.getPersonalInfo().softDelete();

        log.info("Employer with uuid = {" + uuid + "} was soft deleted successfully");

        return mapper.mapToEmployerReadOnlyDTO(employer);
    }

    @Override
    public EmployerDetailsReadOnlyDTO getSingleEmployer(UUID uuid) throws EntityNotFoundException {
        Employer employer = getEmployerByUuid(uuid);
        return mapper.mapToEmployerDetailsReadOnlyDTO(employer);
    }

    @Override
    public EmployerDetailsReadOnlyDTO getSingleEmployerDeletedFalse(UUID uuid) throws EntityNotFoundException {
        Employer employer = getEmployerByUuidDeletedFalse(uuid);
        return mapper.mapToEmployerDetailsReadOnlyDTO(employer);
    }


    // === Utility service methods ===

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
}
