package io.github.dafnipapado.careerstart_backend.controller;

import io.github.dafnipapado.careerstart_backend.core.exception.DataValidationException;
import io.github.dafnipapado.careerstart_backend.core.exception.EntityAlreadyExistsException;
import io.github.dafnipapado.careerstart_backend.core.exception.EntityNotFoundException;
import io.github.dafnipapado.careerstart_backend.core.exception.FileUploadException;
import io.github.dafnipapado.careerstart_backend.dto.job_seeker.*;
import io.github.dafnipapado.careerstart_backend.filters.JobSeekerFilters;
import io.github.dafnipapado.careerstart_backend.service.IJobSeekerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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

    @PatchMapping(value = "/{uuid}")
    public ResponseEntity<JobSeekerReadOnlyDTO> delete(@PathVariable("uuid") UUID uuid)
            throws EntityNotFoundException {

        JobSeekerReadOnlyDTO jobSeekerReadOnlyDTO = jobSeekerService.delete(uuid);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(jobSeekerReadOnlyDTO);
    }

    @GetMapping(value = "/{uuid}/view")
    public ResponseEntity<JobSeekerDetailsReadOnlyDTO> getSingleJobSeeker(@PathVariable("uuid") UUID uuid)
            throws EntityNotFoundException {

        JobSeekerDetailsReadOnlyDTO jobSeekerDetailsReadOnlyDTO = jobSeekerService.getSingleJobSeeker(uuid);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(jobSeekerDetailsReadOnlyDTO);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<JobSeekerDetailsReadOnlyDTO> getSingleJobSeekerDeletedFalse(@PathVariable("uuid") UUID uuid)
            throws EntityNotFoundException {

        JobSeekerDetailsReadOnlyDTO jobSeekerDetailsReadOnlyDTO = jobSeekerService.getSingleJobSeekerDeletedFalse(uuid);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(jobSeekerDetailsReadOnlyDTO);
    }

    @PostMapping("/{uuid}/avatar")
    public ResponseEntity<Void> uploadPictureFile(@PathVariable("uuid") UUID uuid, @RequestParam("picture") MultipartFile file)
            throws EntityNotFoundException, FileUploadException {

        jobSeekerService.uploadDocument(uuid, file);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{uuid}/cv-file")
    public ResponseEntity<Void> uploadCvFile(@PathVariable("uuid") UUID uuid, @RequestParam("cv") MultipartFile file)
            throws EntityNotFoundException, FileUploadException {

        jobSeekerService.uploadDocument(uuid, file);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<JobSeekerSummaryReadOnlyDTO>> getPaginatedFilteredJobSeekers(@ModelAttribute JobSeekerFilters jobSeekerFilters)
            throws EntityNotFoundException {
        Page<JobSeekerSummaryReadOnlyDTO> pagesDTO = jobSeekerService.getPaginatedFilteredJobSeekers(jobSeekerFilters);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pagesDTO);
    }

    @GetMapping("/me")
    public ResponseEntity<JobSeekerDetailsReadOnlyDTO> getCurrentJobSeeker() {
        JobSeekerDetailsReadOnlyDTO jobSeekerDetailsReadOnlyDTO = jobSeekerService.getCurrentJobSeeker();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(jobSeekerDetailsReadOnlyDTO);
    }
}
