package io.github.dafnipapado.careerstart_backend.service;

import io.github.dafnipapado.careerstart_backend.core.exception.EntityAlreadyExistsException;
import io.github.dafnipapado.careerstart_backend.core.exception.EntityNotFoundException;
import io.github.dafnipapado.careerstart_backend.model.Application;
import io.github.dafnipapado.careerstart_backend.model.JobListing;
import io.github.dafnipapado.careerstart_backend.model.JobSeeker;

import java.util.UUID;

public interface IApplicationService {
    void accept(UUID jobSeekerUuid, UUID jobListingUuid) throws EntityNotFoundException, EntityAlreadyExistsException;
    void reject(UUID jobSeekerUuid, UUID jobListingUuid) throws EntityNotFoundException, EntityAlreadyExistsException;

    Application getApplicationByJobSeekerUuidAndJobListingUuid(UUID jobSeekerUuid, UUID jobListingUuid) throws EntityNotFoundException;
    Application getApplicationByJobSeekerUuidAndJobListingUuidDeletedFalse(UUID jobSeekerUuid, UUID jobListingUuid) throws EntityNotFoundException;
    String getStatus(JobSeeker jobSeeker, JobListing jobListing);
}
