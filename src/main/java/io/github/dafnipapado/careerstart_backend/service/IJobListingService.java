package io.github.dafnipapado.careerstart_backend.service;

import io.github.dafnipapado.careerstart_backend.core.exception.EntityNotFoundException;
import io.github.dafnipapado.careerstart_backend.dto.job_listing.JobListingInsertDTO;
import io.github.dafnipapado.careerstart_backend.dto.job_listing.JobListingReadOnlyDTO;
import io.github.dafnipapado.careerstart_backend.dto.job_listing.JobListingUpdateDTO;
import io.github.dafnipapado.careerstart_backend.model.JobListing;

import java.util.UUID;

public interface IJobListingService {
    JobListingReadOnlyDTO save(JobListingInsertDTO jobListingInsertDTO) throws EntityNotFoundException;
    JobListingReadOnlyDTO update(JobListingUpdateDTO jobListingUpdateDTO) throws EntityNotFoundException;
    JobListingReadOnlyDTO delete(UUID uuid) throws EntityNotFoundException;

    JobListing getJobListingByUuid(UUID uuid) throws EntityNotFoundException;
}