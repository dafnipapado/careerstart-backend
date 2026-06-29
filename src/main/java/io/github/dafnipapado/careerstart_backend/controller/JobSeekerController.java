package io.github.dafnipapado.careerstart_backend.controller;

import io.github.dafnipapado.careerstart_backend.core.exception.DataValidationException;
import io.github.dafnipapado.careerstart_backend.core.exception.EntityAlreadyExistsException;
import io.github.dafnipapado.careerstart_backend.core.exception.EntityNotFoundException;
import io.github.dafnipapado.careerstart_backend.dto.job_seeker.JobSeekerInsertDTO;
import io.github.dafnipapado.careerstart_backend.dto.job_seeker.JobSeekerReadOnlyDTO;
import io.github.dafnipapado.careerstart_backend.dto.job_seeker.JobSeekerUpdateDTO;
import io.github.dafnipapado.careerstart_backend.service.IJobSeekerService;
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
@RequestMapping("/api/v1/jobseekers")
public class JobSeekerController {

    private final IJobSeekerService jobSeekerService;

    @PostMapping
    public ResponseEntity<JobSeekerReadOnlyDTO> save(@Valid @RequestBody JobSeekerInsertDTO jobSeekerInsertDTO, BindingResult bindingResult)
            throws EntityNotFoundException, EntityAlreadyExistsException, DataValidationException {

        if (bindingResult.hasErrors()) {
            throw new DataValidationException("JobSeeker", "Job Seeker data validation failed during save.", bindingResult);
        }

        JobSeekerReadOnlyDTO jobSeekerReadOnlyDTO = jobSeekerService.save(jobSeekerInsertDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{uuid}")
                .buildAndExpand(jobSeekerReadOnlyDTO.uuid())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(jobSeekerReadOnlyDTO);
    }

    @PutMapping(value = "/{uuid}")
    public ResponseEntity<JobSeekerReadOnlyDTO> update(@PathVariable("uuid") UUID uuid, @Valid @RequestBody JobSeekerUpdateDTO jobSeekerUpdateDTO, BindingResult bindingResult)
            throws EntityNotFoundException, EntityAlreadyExistsException, DataValidationException {

        if (bindingResult.hasErrors()) {
            throw new DataValidationException("JobSeeker", "Job seeker data validation failed during update.", bindingResult);
        }

        JobSeekerReadOnlyDTO jobSeekerReadOnlyDTO = jobSeekerService.update(jobSeekerUpdateDTO);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(jobSeekerReadOnlyDTO);
    }

}
