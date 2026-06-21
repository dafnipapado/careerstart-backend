package io.github.dafnipapado.careerstart_backend.service;

import io.github.dafnipapado.careerstart_backend.core.exception.EntityAlreadyExistsException;
import io.github.dafnipapado.careerstart_backend.core.exception.EntityNotFoundException;
import io.github.dafnipapado.careerstart_backend.dto.employer.EmployerInsertDTO;
import io.github.dafnipapado.careerstart_backend.dto.employer.EmployerReadOnlyDTO;

public interface IEmployerService {

    EmployerReadOnlyDTO save(EmployerInsertDTO employerInsertDTO) throws EntityNotFoundException, EntityAlreadyExistsException;
}
