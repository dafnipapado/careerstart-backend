package io.github.dafnipapado.careerstart_backend.service;

import io.github.dafnipapado.careerstart_backend.core.exception.EntityAlreadyExistsException;
import io.github.dafnipapado.careerstart_backend.core.exception.EntityNotFoundException;
import io.github.dafnipapado.careerstart_backend.dto.employer.EmployerInsertDTO;
import io.github.dafnipapado.careerstart_backend.dto.employer.EmployerReadOnlyDTO;
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

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployerServiceImpl implements IEmployerService{

    private final Mapper mapper;
    private final ProfessionalFieldRepository professionalFieldRepository;
    private final RoleRepository roleRepository;
    private final RegionRepository regionRepository;
    private final EmployerRepository employerRepository;
    private final UserRepository userRepository;
    private final IUserService userService;
    private final IPersonalInfoService personalInfoService;
    private final PasswordEncoder passwordEncoder;
    private final static Long employerRoleId = 2L;

    @Override
    @Transactional(rollbackFor = {EntityNotFoundException.class, EntityAlreadyExistsException.class})
    public EmployerReadOnlyDTO save(EmployerInsertDTO employerInsertDTO) throws EntityNotFoundException, EntityAlreadyExistsException {

        //checks for vat and username
        if (employerRepository.findByVat(employerInsertDTO.vat()).isPresent()) {
            throw new EntityAlreadyExistsException("Employer", "Employer with vat = '" + employerInsertDTO.vat() + "' already exists.");
        }
        if (userRepository.findByUsername(employerInsertDTO.userInsertDTO().username()).isPresent()) {
            throw new EntityAlreadyExistsException("User", "User with username = '" + employerInsertDTO.userInsertDTO().username() + "' already exists.");
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
        log.info("Employer '" + employerInsertDTO.brandName() + "' was saved successfully.");

        return mapper.mapToEmployerReadOnlyDTO(employer);
    }

    @Override
    public Employer getEmployerByUuid(UUID uuid) throws EntityNotFoundException {
        return employerRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Employer", "Employer with uuid = '" + uuid + "' not found."));
    }

    @Override
    public ProfessionalField getProfessionalFieldById(Long professionalFieldId) throws EntityNotFoundException {
        return professionalFieldRepository.findById(professionalFieldId)
                .orElseThrow(() -> new EntityNotFoundException("ProfessionalField", "Professional field with id = '" + professionalFieldId + "' not found."));
    }
}
