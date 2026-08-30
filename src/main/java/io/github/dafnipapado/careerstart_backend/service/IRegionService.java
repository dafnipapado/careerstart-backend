package io.github.dafnipapado.careerstart_backend.service;

import io.github.dafnipapado.careerstart_backend.dto.RegionReadOnlyDTO;

import java.util.List;

public interface IRegionService {
    List<RegionReadOnlyDTO> getAllRegions();
}
