package io.github.dafnipapado.careerstart_backend.repository;

import io.github.dafnipapado.careerstart_backend.dto.RegionReadOnlyDTO;
import io.github.dafnipapado.careerstart_backend.model.static_data.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegionRepository extends JpaRepository<Region, Long> {
    List<RegionReadOnlyDTO> findAllByOrderByNameAsc();
}
