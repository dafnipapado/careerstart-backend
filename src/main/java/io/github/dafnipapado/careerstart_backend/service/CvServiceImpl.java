package io.github.dafnipapado.careerstart_backend.service;

import io.github.dafnipapado.careerstart_backend.core.exception.EntityNotFoundException;
import io.github.dafnipapado.careerstart_backend.dto.cv.CvInsertDTO;
import io.github.dafnipapado.careerstart_backend.dto.cv.CvReadOnlyDTO;
import io.github.dafnipapado.careerstart_backend.dto.cv.CvUpdateDTO;
import io.github.dafnipapado.careerstart_backend.mapper.Mapper;
import io.github.dafnipapado.careerstart_backend.model.JobSeeker;
import io.github.dafnipapado.careerstart_backend.model.JobSeekerCv;
import io.github.dafnipapado.careerstart_backend.repository.JobSeekerCvRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CvServiceImpl implements ICvService{

    private final IUserService userService;
    private final IJobSeekerService jobSeekerService;
    private final JobSeekerCvRepository jobSeekerCvRepository;
    private final Mapper mapper;

    @Override
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public CvReadOnlyDTO save(CvInsertDTO cvInsertDTO) throws EntityNotFoundException{
        JobSeekerCv jobSeekerCv = mapper.mapToJobSeekerCvEntity(cvInsertDTO);
        jobSeekerCv.setJobSeeker(userService.getCurrentUser().getJobSeeker());
        jobSeekerCvRepository.save(jobSeekerCv);

        log.info("Successful cv creation for jobseeker with uuid = {{}}", jobSeekerCv.getJobSeeker().getUuid());
        return mapper.mapToCvReadOnlyDTO(jobSeekerCv);
    }

    @Override
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public CvReadOnlyDTO update(CvUpdateDTO cvUpdateDTO) throws EntityNotFoundException {
        JobSeekerCv jobSeekerCv = getCvByUuid(cvUpdateDTO.uuid());
        jobSeekerCv.setProfession(cvUpdateDTO.profession());
        jobSeekerCv.setBio(cvUpdateDTO.bio());
        jobSeekerCv.setEducation(cvUpdateDTO.education());
        jobSeekerCv.setExperience(cvUpdateDTO.experience());
        jobSeekerCv.setCertificates(cvUpdateDTO.certificates());
        jobSeekerCv.setLanguages(cvUpdateDTO.languages());
        jobSeekerCv.setSkills(cvUpdateDTO.skills());

        log.info("Cv with uuid = {{}} was updated successfully", jobSeekerCv.getUuid());
        return mapper.mapToCvReadOnlyDTO(jobSeekerCv);
    }

    @Override
    public CvReadOnlyDTO getJobSeekerCv(UUID jobSeekerUuid) throws EntityNotFoundException {
        JobSeeker jobSeeker = jobSeekerService.getJobSeekerByUuid(jobSeekerUuid);
        JobSeekerCv jobSeekerCv = jobSeeker.getJobSeekerCv();
        return mapper.mapToCvReadOnlyDTO(jobSeekerCv);
    }

    @Override
    public JobSeekerCv getCvByUuid(UUID uuid) throws EntityNotFoundException {
        return jobSeekerCvRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("JobSeekerCv", "Cv with uuid = {" + uuid + "} not found."));
    }
}
