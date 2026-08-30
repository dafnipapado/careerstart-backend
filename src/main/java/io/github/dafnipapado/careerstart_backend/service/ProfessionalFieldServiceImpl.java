package io.github.dafnipapado.careerstart_backend.service;

import io.github.dafnipapado.careerstart_backend.dto.ProfessionalFieldReadOnlyDTO;
import io.github.dafnipapado.careerstart_backend.repository.ProfessionalFieldRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfessionalFieldServiceImpl implements IProfessionalFieldService{

    private final ProfessionalFieldRepository professionalFieldRepository;

    @Override
    public List<ProfessionalFieldReadOnlyDTO> getAllProfessionalFields() {
        return professionalFieldRepository.findAllByOrderByNameAsc();
    }
}
