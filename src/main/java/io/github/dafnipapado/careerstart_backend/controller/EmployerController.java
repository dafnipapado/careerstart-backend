package io.github.dafnipapado.careerstart_backend.controller;

import io.github.dafnipapado.careerstart_backend.core.exception.DataValidationException;
import io.github.dafnipapado.careerstart_backend.core.exception.EntityAlreadyExistsException;
import io.github.dafnipapado.careerstart_backend.core.exception.EntityNotFoundException;
import io.github.dafnipapado.careerstart_backend.dto.employer.EmployerDetailsReadOnlyDTO;
import io.github.dafnipapado.careerstart_backend.dto.employer.EmployerInsertDTO;
import io.github.dafnipapado.careerstart_backend.dto.employer.EmployerReadOnlyDTO;
import io.github.dafnipapado.careerstart_backend.dto.employer.EmployerUpdateDTO;
import io.github.dafnipapado.careerstart_backend.service.IEmployerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/employers")
public class EmployerController {

    private final IEmployerService employerService;

    @PostMapping
    public ResponseEntity<EmployerReadOnlyDTO> save(@Valid @RequestBody EmployerInsertDTO employerInsertDTO, BindingResult bindingResult)
            throws EntityNotFoundException, EntityAlreadyExistsException, DataValidationException {

        if (bindingResult.hasErrors()) {
            throw new DataValidationException("Employer", "Employer data validation failed during save.", bindingResult);
        }

        EmployerReadOnlyDTO employerReadOnlyDTO = employerService.save(employerInsertDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{uuid}")
                .buildAndExpand(employerReadOnlyDTO.uuid())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(employerReadOnlyDTO);
    }

    @PutMapping(value = "/{uuid}")
    public ResponseEntity<EmployerReadOnlyDTO> update(@PathVariable("uuid") UUID uuid, @Valid @RequestBody EmployerUpdateDTO employerUpdateDTO, BindingResult bindingResult)
            throws EntityNotFoundException, EntityAlreadyExistsException, DataValidationException {

        if (bindingResult.hasErrors()) {
            throw new DataValidationException("Employer", "Employer data validation failed during update", bindingResult);
        }

        EmployerReadOnlyDTO employerReadOnlyDTO = employerService.update(employerUpdateDTO);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(employerReadOnlyDTO);
    }

    @PatchMapping(value = "/{uuid}")
    public ResponseEntity<EmployerReadOnlyDTO> delete(@PathVariable("uuid") UUID uuid)
            throws EntityNotFoundException {

        EmployerReadOnlyDTO employerReadOnlyDTO = employerService.delete(uuid);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(employerReadOnlyDTO);
    }

    @GetMapping(value = "/{uuid}")
    public ResponseEntity<EmployerDetailsReadOnlyDTO> getSingleEmployer(@PathVariable("uuid") UUID uuid)
            throws EntityNotFoundException {

        EmployerDetailsReadOnlyDTO employerDetailsReadOnlyDTO = employerService.getSingleEmployer(uuid);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(employerDetailsReadOnlyDTO);
    }

    @GetMapping("/{uuid}/profile")
    public ResponseEntity<EmployerDetailsReadOnlyDTO> getSingleEmployerDeletedFalse(@PathVariable UUID uuid)
            throws EntityNotFoundException {

        EmployerDetailsReadOnlyDTO employerDetailsReadOnlyDTO = employerService.getSingleEmployerDeletedFalse(uuid);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(employerDetailsReadOnlyDTO);
    }

    @PostMapping("/{uuid}/picture")
    public ResponseEntity<Void> uploadPicture(@PathVariable UUID uuid, @RequestParam("picture") MultipartFile file)
            throws EntityNotFoundException, IOException {

        employerService.uploadPicture(uuid, file);

        return ResponseEntity.noContent().build();
    }
}
