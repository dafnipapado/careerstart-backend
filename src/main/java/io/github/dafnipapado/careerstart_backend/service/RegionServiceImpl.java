package io.github.dafnipapado.careerstart_backend.service;

import io.github.dafnipapado.careerstart_backend.core.exception.EntityNotFoundException;
import io.github.dafnipapado.careerstart_backend.dto.RegionReadOnlyDTO;
import io.github.dafnipapado.careerstart_backend.model.static_data.Region;
import io.github.dafnipapado.careerstart_backend.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegionServiceImpl implements IRegionService {

    private final RegionRepository regionRepository;

    @Override
    public List<RegionReadOnlyDTO> getAllRegions() {
        return regionRepository.findAllByOrderByNameAsc();
    }

    @Override
    public Region getRegionById(Long regionId) throws EntityNotFoundException {
        return regionRepository.findById(regionId)
                .orElseThrow(() -> new EntityNotFoundException("Region", "Region with id = {" + regionId + "} not found."));
    }
}
