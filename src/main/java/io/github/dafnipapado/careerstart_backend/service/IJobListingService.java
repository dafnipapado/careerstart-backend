package io.github.dafnipapado.careerstart_backend.service;

import io.github.dafnipapado.careerstart_backend.core.exception.EntityNotFoundException;
import io.github.dafnipapado.careerstart_backend.dto.job_listing.*;
import io.github.dafnipapado.careerstart_backend.filters.JobListingFilters;
import io.github.dafnipapado.careerstart_backend.model.JobListing;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface IJobListingService {
    JobListingReadOnlyDTO save(JobListingInsertDTO jobListingInsertDTO) throws EntityNotFoundException;
    JobListingReadOnlyDTO update(JobListingUpdateDTO jobListingUpdateDTO) throws EntityNotFoundException;
    JobListingReadOnlyDTO delete(UUID uuid) throws EntityNotFoundException;

    JobListingDetailsReadOnlyDTO getSingleJobListing(UUID uuid) throws EntityNotFoundException;
    JobListingDetailsReadOnlyDTO getSingleJobListingDeletedFalse(UUID uuid) throws EntityNotFoundException;

    Page<JobListingSummaryReadOnlyDTO> getPaginatedFilteredJobListings(JobListingFilters jobListingFilters) throws EntityNotFoundException;

    JobListing getJobListingByUuid(UUID uuid) throws EntityNotFoundException;
    JobListing getJobListingByUuidDeletedFalse(UUID uuid) throws EntityNotFoundException;

}