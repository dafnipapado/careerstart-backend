package io.github.dafnipapado.careerstart_backend.service;

import io.github.dafnipapado.careerstart_backend.core.exception.EntityAlreadyExistsException;
import io.github.dafnipapado.careerstart_backend.core.exception.EntityNotFoundException;
import io.github.dafnipapado.careerstart_backend.dto.job_seeker.JobSeekerInsertDTO;
import io.github.dafnipapado.careerstart_backend.dto.job_seeker.JobSeekerReadOnlyDTO;
import io.github.dafnipapado.careerstart_backend.mapper.Mapper;
import io.github.dafnipapado.careerstart_backend.model.JobSeeker;
import io.github.dafnipapado.careerstart_backend.model.static_data.Region;
import io.github.dafnipapado.careerstart_backend.model.static_data.Role;
import io.github.dafnipapado.careerstart_backend.repository.JobSeekerRepository;
import io.github.dafnipapado.careerstart_backend.repository.PersonalInfoRepository;
import io.github.dafnipapado.careerstart_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobSeekerServiceImpl implements IJobSeekerService{

    private final IUserService userService;
    private final IPersonalInfoService personalInfoService;
    private final JobSeekerRepository jobSeekerRepository;
    private final UserRepository userRepository;
    private final PersonalInfoRepository personalInfoRepository;
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
        log.info("Job seeker //{ {} {} //} was saved successfully.", jobSeekerInsertDTO.firstname(), jobSeekerInsertDTO.lastname());

        return mapper.mapToJobSeekerReadOnlyDTO(jobSeeker);
    }

}
