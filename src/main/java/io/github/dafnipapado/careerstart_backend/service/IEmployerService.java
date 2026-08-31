package io.github.dafnipapado.careerstart_backend.service;

import io.github.dafnipapado.careerstart_backend.core.exception.EntityAlreadyExistsException;
import io.github.dafnipapado.careerstart_backend.core.exception.EntityNotFoundException;
import io.github.dafnipapado.careerstart_backend.core.exception.FileUploadException;
import io.github.dafnipapado.careerstart_backend.dto.employer.*;
import io.github.dafnipapado.careerstart_backend.filters.EmployerFilters;
import io.github.dafnipapado.careerstart_backend.model.Employer;
import io.github.dafnipapado.careerstart_backend.model.static_data.ProfessionalField;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface IEmployerService {
    EmployerReadOnlyDTO save(EmployerInsertDTO employerInsertDTO) throws EntityNotFoundException, EntityAlreadyExistsException;
    EmployerReadOnlyDTO update(EmployerUpdateDTO employerUpdateDTO) throws EntityNotFoundException, EntityAlreadyExistsException;
    EmployerReadOnlyDTO delete(UUID uuid) throws EntityNotFoundException;

    EmployerDetailsReadOnlyDTO getSingleEmployer(UUID uuid) throws EntityNotFoundException;
    EmployerDetailsReadOnlyDTO getSingleEmployerDeletedFalse(UUID uuid) throws EntityNotFoundException;

    void uploadAttachment(UUID uuid, MultipartFile file) throws EntityNotFoundException, FileUploadException;

    Page<EmployerSummaryReadOnlyDTO> getPaginatedFilteredEmployers(EmployerFilters employerFilters) throws EntityNotFoundException;

    EmployerDetailsReadOnlyDTO getCurrentEmployer();

    Employer getEmployerByUuid(UUID uuid) throws EntityNotFoundException;
    Employer getEmployerByUuidDeletedFalse(UUID uuid) throws EntityNotFoundException;
    ProfessionalField getProfessionalFieldById(Long professionalFieldId) throws EntityNotFoundException;


}
