package io.github.dafnipapado.careerstart_backend.service;

import io.github.dafnipapado.careerstart_backend.core.exception.EntityAlreadyExistsException;
import io.github.dafnipapado.careerstart_backend.core.exception.EntityNotFoundException;
import io.github.dafnipapado.careerstart_backend.dto.job_seeker.JobSeekerInsertDTO;
import io.github.dafnipapado.careerstart_backend.dto.job_seeker.JobSeekerReadOnlyDTO;
import io.github.dafnipapado.careerstart_backend.dto.job_seeker.JobSeekerUpdateDTO;
import io.github.dafnipapado.careerstart_backend.model.JobSeeker;

import java.util.UUID;

public interface IJobSeekerService {

    JobSeekerReadOnlyDTO save(JobSeekerInsertDTO jobSeekerInsertDTO) throws EntityNotFoundException, EntityAlreadyExistsException;
    JobSeekerReadOnlyDTO update(JobSeekerUpdateDTO jobSeekerUpdateDTO) throws EntityNotFoundException, EntityAlreadyExistsException;
    JobSeekerReadOnlyDTO delete(UUID uuid) throws EntityNotFoundException;

    JobSeeker getJobSeekerByUuid(UUID uuid) throws EntityNotFoundException;
}
