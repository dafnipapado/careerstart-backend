package io.github.dafnipapado.careerstart_backend.controller;

import io.github.dafnipapado.careerstart_backend.dto.ProfessionalFieldReadOnlyDTO;
import io.github.dafnipapado.careerstart_backend.dto.RegionReadOnlyDTO;
import io.github.dafnipapado.careerstart_backend.service.IProfessionalFieldService;
import io.github.dafnipapado.careerstart_backend.service.IRegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class StaticDataController {

    private final IProfessionalFieldService professionalFieldService;
    private final IRegionService regionService;

    @GetMapping("/fields")
    public ResponseEntity<List<ProfessionalFieldReadOnlyDTO>> getAllFields() {
        List<ProfessionalFieldReadOnlyDTO> fields = professionalFieldService.getAllProfessionalFields();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(fields);
    }

    @GetMapping("/regions")
    public ResponseEntity<List<RegionReadOnlyDTO>> getAllRegions() {
        List<RegionReadOnlyDTO> regions = regionService.getAllRegions();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(regions);
    }
}
