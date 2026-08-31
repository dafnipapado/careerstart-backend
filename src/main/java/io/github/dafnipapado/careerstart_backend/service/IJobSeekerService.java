package io.github.dafnipapado.careerstart_backend.service;

import io.github.dafnipapado.careerstart_backend.core.exception.EntityAlreadyExistsException;
import io.github.dafnipapado.careerstart_backend.core.exception.EntityNotFoundException;
import io.github.dafnipapado.careerstart_backend.core.exception.FileUploadException;
import io.github.dafnipapado.careerstart_backend.dto.employer.EmployerSummaryReadOnlyDTO;
import io.github.dafnipapado.careerstart_backend.dto.job_seeker.*;
import io.github.dafnipapado.careerstart_backend.filters.EmployerFilters;
import io.github.dafnipapado.careerstart_backend.filters.JobSeekerFilters;
import io.github.dafnipapado.careerstart_backend.model.JobSeeker;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface IJobSeekerService {

    JobSeekerReadOnlyDTO save(JobSeekerInsertDTO jobSeekerInsertDTO) throws EntityNotFoundException, EntityAlreadyExistsException;
    JobSeekerReadOnlyDTO update(JobSeekerUpdateDTO jobSeekerUpdateDTO) throws EntityNotFoundException, EntityAlreadyExistsException;
    JobSeekerReadOnlyDTO delete(UUID uuid) throws EntityNotFoundException;

    JobSeekerDetailsReadOnlyDTO getSingleJobSeeker(UUID uuid) throws EntityNotFoundException;
    JobSeekerDetailsReadOnlyDTO getSingleJobSeekerDeletedFalse(UUID uuid) throws EntityNotFoundException;

    void uploadDocument(UUID uuid, MultipartFile file) throws EntityNotFoundException, FileUploadException;

    Page<JobSeekerSummaryReadOnlyDTO> getPaginatedFilteredJobSeekers(JobSeekerFilters jobSeekerFilters) throws EntityNotFoundException;

    JobSeekerDetailsReadOnlyDTO getCurrentJobSeeker();

    JobSeeker getJobSeekerByUuid(UUID uuid) throws EntityNotFoundException;
    JobSeeker getJobSeekerByUuidDeletedFalse(UUID uuid) throws EntityNotFoundException;
}
