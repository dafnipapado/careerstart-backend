package io.github.dafnipapado.careerstart_backend.service;

import io.github.dafnipapado.careerstart_backend.core.exception.EntityNotFoundException;
import io.github.dafnipapado.careerstart_backend.dto.RegionReadOnlyDTO;
import io.github.dafnipapado.careerstart_backend.model.static_data.Region;

import java.util.List;

public interface IRegionService {
    List<RegionReadOnlyDTO> getAllRegions();
    Region getRegionById(Long regionId) throws EntityNotFoundException;
}
