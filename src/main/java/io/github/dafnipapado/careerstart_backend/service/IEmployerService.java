package io.github.dafnipapado.careerstart_backend.service;

import io.github.dafnipapado.careerstart_backend.core.exception.EntityAlreadyExistsException;
import io.github.dafnipapado.careerstart_backend.core.exception.EntityNotFoundException;
import io.github.dafnipapado.careerstart_backend.dto.employer.EmployerInsertDTO;
import io.github.dafnipapado.careerstart_backend.dto.employer.EmployerReadOnlyDTO;
import io.github.dafnipapado.careerstart_backend.dto.employer.EmployerUpdateDTO;
import io.github.dafnipapado.careerstart_backend.model.Employer;
import io.github.dafnipapado.careerstart_backend.model.static_data.ProfessionalField;

import java.util.UUID;

public interface IEmployerService {
    EmployerReadOnlyDTO save(EmployerInsertDTO employerInsertDTO) throws EntityNotFoundException, EntityAlreadyExistsException;
    EmployerReadOnlyDTO update(EmployerUpdateDTO employerUpdateDTO) throws EntityNotFoundException, EntityAlreadyExistsException;
    EmployerReadOnlyDTO delete(UUID uuid) throws EntityNotFoundException;

    Employer getEmployerByUuid(UUID uuid) throws EntityNotFoundException;
    ProfessionalField getProfessionalFieldById(Long professionalFieldId) throws EntityNotFoundException;


}
