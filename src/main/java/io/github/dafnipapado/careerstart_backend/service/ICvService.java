package io.github.dafnipapado.careerstart_backend.service;

import io.github.dafnipapado.careerstart_backend.core.exception.EntityNotFoundException;
import io.github.dafnipapado.careerstart_backend.dto.cv.CvInsertDTO;
import io.github.dafnipapado.careerstart_backend.dto.cv.CvReadOnlyDTO;
import io.github.dafnipapado.careerstart_backend.dto.cv.CvUpdateDTO;
import io.github.dafnipapado.careerstart_backend.model.JobSeekerCv;

import java.util.UUID;

public interface ICvService {
    CvReadOnlyDTO save(CvInsertDTO cvInsertDTO) throws EntityNotFoundException;
    CvReadOnlyDTO update(CvUpdateDTO cvUpdateDTO) throws EntityNotFoundException;
    CvReadOnlyDTO getJobSeekerCv(UUID uuid) throws EntityNotFoundException;
    JobSeekerCv getCvByUuid(UUID uuid) throws EntityNotFoundException;

}
