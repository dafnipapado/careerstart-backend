package io.github.dafnipapado.careerstart_backend.service;

import io.github.dafnipapado.careerstart_backend.core.exception.EntityNotFoundException;
import io.github.dafnipapado.careerstart_backend.dto.job_listing.JobListingInsertDTO;
import io.github.dafnipapado.careerstart_backend.dto.job_listing.JobListingReadOnlyDTO;
import io.github.dafnipapado.careerstart_backend.mapper.Mapper;
import io.github.dafnipapado.careerstart_backend.model.Employer;
import io.github.dafnipapado.careerstart_backend.model.JobListing;
import io.github.dafnipapado.careerstart_backend.model.static_data.ProfessionalField;
import io.github.dafnipapado.careerstart_backend.model.static_data.Region;
import io.github.dafnipapado.careerstart_backend.repository.EmployerRepository;
import io.github.dafnipapado.careerstart_backend.repository.JobListingRepository;
import io.github.dafnipapado.careerstart_backend.repository.ProfessionalFieldRepository;
import io.github.dafnipapado.careerstart_backend.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobListingServiceImpl implements IJobListingService{

    private final Mapper mapper;
    private final JobListingRepository jobListingRepository;
    private final ProfessionalFieldRepository professionalFieldRepository;
    private final EmployerRepository employerRepository;
    private final RegionRepository regionRepository;
    private final IEmployerService employerService;
    private final IPersonalInfoService personalInfoService;

    @Override
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public JobListingReadOnlyDTO save(JobListingInsertDTO jobListingInsertDTO) throws EntityNotFoundException {
        JobListing jobListing = mapper.mapToJobListingEntity(jobListingInsertDTO);

        Employer employer = employerRepository.findById(jobListingInsertDTO.employerId())
                .orElseThrow(() -> new EntityNotFoundException("Employer", "Employer not found."));
        jobListing.setEmployer(employer);
        ProfessionalField professionalField = employerService.getProfessionalFieldById(jobListingInsertDTO.professionalFieldId());
        jobListing.setProfessionalField(professionalField);
        Region region = personalInfoService.getRegionById(jobListingInsertDTO.regionId());
        jobListing.setRegion(region);

        jobListingRepository.save(jobListing);
        log.info("Job listing {{}} with uuid = {{}} by employer = {{}} was saved successfully.", jobListing.getTitle(), jobListing.getUuid(), employer.getBrandName());

        return mapper.mapToJobListingReadOnlyDTO(jobListing);
    }
}
