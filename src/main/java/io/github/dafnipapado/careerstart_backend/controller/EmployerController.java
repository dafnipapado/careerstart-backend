package io.github.dafnipapado.careerstart_backend.controller;

import io.github.dafnipapado.careerstart_backend.core.exception.DataValidationException;
import io.github.dafnipapado.careerstart_backend.core.exception.EntityAlreadyExistsException;
import io.github.dafnipapado.careerstart_backend.core.exception.EntityNotFoundException;
import io.github.dafnipapado.careerstart_backend.dto.employer.EmployerInsertDTO;
import io.github.dafnipapado.careerstart_backend.dto.employer.EmployerReadOnlyDTO;
import io.github.dafnipapado.careerstart_backend.service.IEmployerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/employers")
public class EmployerController {

    private final IEmployerService employerService;

    @PostMapping
    public ResponseEntity<EmployerReadOnlyDTO> save(@RequestBody EmployerInsertDTO employerInsertDTO, BindingResult bindingResult)
            throws EntityNotFoundException, EntityAlreadyExistsException, DataValidationException {

        if (bindingResult.hasErrors()) {
            throw new DataValidationException("Employer", "Employer data validation failed.", bindingResult);
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
}
