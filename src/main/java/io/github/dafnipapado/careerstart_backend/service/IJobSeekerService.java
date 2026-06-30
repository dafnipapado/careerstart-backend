package io.github.dafnipapado.careerstart_backend.service;

import io.github.dafnipapado.careerstart_backend.core.exception.EntityAlreadyExistsException;
import io.github.dafnipapado.careerstart_backend.core.exception.EntityNotFoundException;
import io.github.dafnipapado.careerstart_backend.core.exception.FileUploadException;
import io.github.dafnipapado.careerstart_backend.dto.job_seeker.JobSeekerDetailsReadOnlyDTO;
import io.github.dafnipapado.careerstart_backend.dto.job_seeker.JobSeekerInsertDTO;
import io.github.dafnipapado.careerstart_backend.dto.job_seeker.JobSeekerReadOnlyDTO;
import io.github.dafnipapado.careerstart_backend.dto.job_seeker.JobSeekerUpdateDTO;
import io.github.dafnipapado.careerstart_backend.model.JobSeeker;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface IJobSeekerService {

    JobSeekerReadOnlyDTO save(JobSeekerInsertDTO jobSeekerInsertDTO) throws EntityNotFoundException, EntityAlreadyExistsException;
    JobSeekerReadOnlyDTO update(JobSeekerUpdateDTO jobSeekerUpdateDTO) throws EntityNotFoundException, EntityAlreadyExistsException;
    JobSeekerReadOnlyDTO delete(UUID uuid) throws EntityNotFoundException;

    JobSeekerDetailsReadOnlyDTO getSingleJobSeeker(UUID uuid) throws EntityNotFoundException;
    JobSeekerDetailsReadOnlyDTO getSingleJobSeekerDeletedFalse(UUID uuid) throws EntityNotFoundException;

    void uploadDocument(UUID uuid, MultipartFile file) throws EntityNotFoundException, FileUploadException;

    JobSeeker getJobSeekerByUuid(UUID uuid) throws EntityNotFoundException;
    JobSeeker getJobSeekerByUuidDeletedFalse(UUID uuid) throws EntityNotFoundException;
}
