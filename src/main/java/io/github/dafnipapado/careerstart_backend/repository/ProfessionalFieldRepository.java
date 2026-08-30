package io.github.dafnipapado.careerstart_backend.repository;

import io.github.dafnipapado.careerstart_backend.dto.ProfessionalFieldReadOnlyDTO;
import io.github.dafnipapado.careerstart_backend.model.static_data.ProfessionalField;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfessionalFieldRepository extends JpaRepository<ProfessionalField, Long> {
    List<ProfessionalFieldReadOnlyDTO> findAllByOrderByNameAsc();
}
