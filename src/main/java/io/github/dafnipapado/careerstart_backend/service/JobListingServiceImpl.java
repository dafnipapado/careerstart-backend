package io.github.dafnipapado.careerstart_backend.service;

import io.github.dafnipapado.careerstart_backend.core.exception.EntityNotFoundException;
import io.github.dafnipapado.careerstart_backend.dto.job_listing.*;
import io.github.dafnipapado.careerstart_backend.filters.JobListingFilters;
import io.github.dafnipapado.careerstart_backend.mapper.Mapper;
import io.github.dafnipapado.careerstart_backend.model.JobListing;
import io.github.dafnipapado.careerstart_backend.model.static_data.ProfessionalField;
import io.github.dafnipapado.careerstart_backend.model.static_data.Region;
import io.github.dafnipapado.careerstart_backend.repository.JobListingRepository;
import io.github.dafnipapado.careerstart_backend.specification.JobListingSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobListingServiceImpl implements IJobListingService{

    private final Mapper mapper;
    private final JobListingRepository jobListingRepository;
    private final IEmployerService employerService;
    private final IRegionService regionService;
    private final IUserService userService;

    @Override
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public JobListingReadOnlyDTO save(JobListingInsertDTO jobListingInsertDTO) throws EntityNotFoundException {
        JobListing jobListing = mapper.mapToJobListingEntity(jobListingInsertDTO);

        jobListing.setEmployer(userService.getCurrentUser().getEmployer());

        ProfessionalField professionalField = employerService.getProfessionalFieldById(jobListingInsertDTO.professionalFieldId());
        jobListing.setProfessionalField(professionalField);
        Region region = regionService.getRegionById(jobListingInsertDTO.regionId());
        jobListing.setRegion(region);

        jobListingRepository.save(jobListing);
        log.info("Job listing {{}} with uuid = {{}} by employer = {{}} was saved successfully.", jobListing.getTitle(), jobListing.getUuid(), jobListing.getEmployer().getBrandName());

        return mapper.mapToJobListingReadOnlyDTO(jobListing);
    }

    @Override
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public JobListingReadOnlyDTO update(JobListingUpdateDTO jobListingUpdateDTO) throws EntityNotFoundException {
        JobListing jobListing = getJobListingByUuid(jobListingUpdateDTO.uuid());
        jobListing.setTitle(jobListingUpdateDTO.title());
        jobListing.setDescription(jobListingUpdateDTO.description());
        if (!Objects.equals(jobListingUpdateDTO.professionalFieldId(), jobListing.getProfessionalField().getId())) {
            ProfessionalField updatedProfessionalField = employerService.getProfessionalFieldById(jobListingUpdateDTO.professionalFieldId());
            jobListing.getProfessionalField().removeJobListing(jobListing);
            updatedProfessionalField.addJobListing(jobListing);
        }
        if (!Objects.equals(jobListingUpdateDTO.regionId(), jobListing.getRegion().getId())) {
            Region updatedRegion = regionService.getRegionById(jobListingUpdateDTO.regionId());
            jobListing.getRegion().removeJobListing(jobListing);
            updatedRegion.addJobListing(jobListing);
        }

        log.info("Job listing {{}} with uuid = {{}} was updated successfully.", jobListing.getTitle(), jobListing.getUuid());
        return mapper.mapToJobListingReadOnlyDTO(jobListing);
    }

    @Override
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public JobListingReadOnlyDTO delete(UUID uuid) throws EntityNotFoundException {
        JobListing jobListing = getJobListingByUuid(uuid);
        jobListing.softDelete();

        log.info("Job listing with uuid = {" + uuid + "} was soft deleted successfully.");

        return mapper.mapToJobListingReadOnlyDTO(jobListing);
    }

    @Override
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public void restore(UUID uuid) throws EntityNotFoundException {
        JobListing jobListing = jobListingRepository.findByUuidAndDeletedTrue(uuid)
                .orElseThrow(() -> new EntityNotFoundException("JobListing", "No deleted job listing with uuid = {" + uuid + "} found"));

        jobListing.activate();
        log.info("Job listing with uuid = {" + uuid + "} was restored successfully");
    }

    @Override
    public JobListingDetailsReadOnlyDTO getSingleJobListing(UUID uuid) throws EntityNotFoundException {
        JobListing jobListing = getJobListingByUuid(uuid);

        log.info("Job listing with uuid = {" + uuid + "} was fetched successfully.");
        return mapper.mapToJobListingDetailsReadOnlyDTO(jobListing);
    }

    @Override
    public JobListingDetailsReadOnlyDTO getSingleJobListingDeletedFalse(UUID uuid) throws EntityNotFoundException {
        JobListing jobListing = getJobListingByUuidDeletedFalse(uuid);

        log.info("Active job listing with uuid = {" + uuid + "} was fetched successfully.");
        return mapper.mapToJobListingDetailsReadOnlyDTO(jobListing);
    }

    @Override
    public Page<JobListingSummaryReadOnlyDTO> getPaginatedFilteredJobListings(JobListingFilters jobListingFilters) throws EntityNotFoundException {
        if (jobListingFilters.getUuid() != null) {
            JobListing jobListing = getJobListingByUuid(jobListingFilters.getUuid());
            return getSingleResultPage(jobListingFilters.getPageable(), jobListing);
        }

        var filtered = jobListingRepository.findAll(JobListingSpecification.build(jobListingFilters), jobListingFilters.getPageable());

        log.info("Filtered {} job listings.", filtered.getNumberOfElements());
        return filtered.map(mapper::mapToJobListingSummaryReadOnlyDTO);
    }

    private Page<JobListingSummaryReadOnlyDTO> getSingleResultPage(Pageable pageable, JobListing jobListing) {
        return new PageImpl<>(
                List.of(mapper.mapToJobListingSummaryReadOnlyDTO(jobListing)),
                pageable,
                1
        );
    }

    @Override
    public JobListing getJobListingByUuid(UUID uuid) throws EntityNotFoundException {
        return jobListingRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("JobListing", "Job listing with uuid = {" + uuid + "} not found."));
    }

    @Override
    public JobListing getJobListingByUuidDeletedFalse(UUID uuid) throws EntityNotFoundException {
        return jobListingRepository.findByUuidAndDeletedFalse(uuid)
                .orElseThrow(() -> new EntityNotFoundException("JobListing", "Active job listing with uuid = {" + uuid + "} not found."));
    }
}
