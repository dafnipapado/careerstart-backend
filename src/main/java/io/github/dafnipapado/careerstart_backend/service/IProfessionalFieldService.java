package io.github.dafnipapado.careerstart_backend.service;

import io.github.dafnipapado.careerstart_backend.dto.ProfessionalFieldReadOnlyDTO;

import java.util.List;

public interface IProfessionalFieldService {
    List<ProfessionalFieldReadOnlyDTO> getAllProfessionalFields();
}
