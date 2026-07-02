package io.github.dafnipapado.careerstart_backend.controller;

import io.github.dafnipapado.careerstart_backend.core.exception.DataValidationException;
import io.github.dafnipapado.careerstart_backend.core.exception.EntityNotFoundException;
import io.github.dafnipapado.careerstart_backend.dto.employer.EmployerDetailsReadOnlyDTO;
import io.github.dafnipapado.careerstart_backend.dto.employer.EmployerReadOnlyDTO;
import io.github.dafnipapado.careerstart_backend.dto.job_listing.JobListingDetailsReadOnlyDTO;
import io.github.dafnipapado.careerstart_backend.dto.job_listing.JobListingInsertDTO;
import io.github.dafnipapado.careerstart_backend.dto.job_listing.JobListingReadOnlyDTO;
import io.github.dafnipapado.careerstart_backend.dto.job_listing.JobListingUpdateDTO;
import io.github.dafnipapado.careerstart_backend.service.IJobListingService;
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
@RequestMapping("/api/v1/job-listings")
public class JobListingController {

    private final IJobListingService jobListingService;

    @PostMapping
    public ResponseEntity<JobListingReadOnlyDTO> save(@Valid @RequestBody JobListingInsertDTO jobListingInsertDTO, BindingResult bindingResult)
            throws EntityNotFoundException, DataValidationException {

        if (bindingResult.hasErrors()) {
            throw new DataValidationException("JobListing", "Job listing data validation failed during save.", bindingResult);
        }

        JobListingReadOnlyDTO jobListingReadOnlyDTO = jobListingService.save(jobListingInsertDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{uuid}")
                .buildAndExpand(jobListingReadOnlyDTO.uuid())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(jobListingReadOnlyDTO);
    }

    @PutMapping(value = "/{uuid}")
    public ResponseEntity<JobListingReadOnlyDTO> update(@PathVariable("uuid") UUID uuid, @Valid @RequestBody JobListingUpdateDTO jobListingUpdateDTO, BindingResult bindingResult)
            throws EntityNotFoundException, DataValidationException {

        if (bindingResult.hasErrors()) {
            throw new DataValidationException("JobListing", "Job listing data validation failed during update", bindingResult);
        }

        JobListingReadOnlyDTO jobListingReadOnlyDTO = jobListingService.update(jobListingUpdateDTO);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(jobListingReadOnlyDTO);
    }

    @PatchMapping(value = "/{uuid}")
    public ResponseEntity<JobListingReadOnlyDTO> delete(@PathVariable("uuid") UUID uuid)
            throws EntityNotFoundException {

        JobListingReadOnlyDTO jobListingReadOnlyDTO = jobListingService.delete(uuid);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(jobListingReadOnlyDTO);
    }

    @GetMapping(value = "/{uuid}/view")
    public ResponseEntity<JobListingDetailsReadOnlyDTO> getSingleJobListing(@PathVariable("uuid") UUID uuid)
            throws EntityNotFoundException {

        JobListingDetailsReadOnlyDTO jobListingDetailsReadOnlyDTO = jobListingService.getSingleJobListing(uuid);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(jobListingDetailsReadOnlyDTO);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<JobListingDetailsReadOnlyDTO> getSingleJobListingDeletedFalse(@PathVariable UUID uuid)
            throws EntityNotFoundException {

        JobListingDetailsReadOnlyDTO jobListingDetailsReadOnlyDTO = jobListingService.getSingleJobListingDeletedFalse(uuid);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(jobListingDetailsReadOnlyDTO);
    }
}
