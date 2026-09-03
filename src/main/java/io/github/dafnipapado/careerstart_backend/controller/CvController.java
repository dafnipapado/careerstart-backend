package io.github.dafnipapado.careerstart_backend.controller;

import io.github.dafnipapado.careerstart_backend.core.exception.DataValidationException;
import io.github.dafnipapado.careerstart_backend.core.exception.EntityNotFoundException;
import io.github.dafnipapado.careerstart_backend.dto.cv.CvInsertDTO;
import io.github.dafnipapado.careerstart_backend.dto.cv.CvReadOnlyDTO;
import io.github.dafnipapado.careerstart_backend.dto.cv.CvUpdateDTO;
import io.github.dafnipapado.careerstart_backend.service.ICvService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cv")
public class CvController {

    private final ICvService cvService;

    @PostMapping
    public ResponseEntity<CvReadOnlyDTO> save(@Valid @RequestBody CvInsertDTO cvInsertDTO, BindingResult bindingResult)
            throws EntityNotFoundException, DataValidationException {

        if (bindingResult.hasErrors()) {
            throw new DataValidationException("JobSeekerCv", "Cv data validation failed during save.", bindingResult);
        }

        CvReadOnlyDTO cvReadOnlyDTO = cvService.save(cvInsertDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{uuid}")
                .buildAndExpand(cvReadOnlyDTO.uuid())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(cvReadOnlyDTO);
    }

    @PutMapping(value = "/{uuid}")
    public ResponseEntity<CvReadOnlyDTO> update(@PathVariable("uuid") UUID uuid, @Valid @RequestBody CvUpdateDTO cvUpdateDTO, BindingResult bindingResult)
            throws EntityNotFoundException, DataValidationException {

        if (bindingResult.hasErrors()) {
            throw new DataValidationException("JobSeekerCv", "Cv data validation failed during update", bindingResult);
        }

        CvReadOnlyDTO cvReadOnlyDTO = cvService.update(cvUpdateDTO);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cvReadOnlyDTO);
    }

    @GetMapping(value = "/{uuid}/view")
    public ResponseEntity<CvReadOnlyDTO> getJobSeekerCv(@PathVariable("uuid") UUID uuid)
            throws EntityNotFoundException {

        CvReadOnlyDTO cvReadOnlyDTO = cvService.getJobSeekerCv(uuid);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cvReadOnlyDTO);
    }
}
