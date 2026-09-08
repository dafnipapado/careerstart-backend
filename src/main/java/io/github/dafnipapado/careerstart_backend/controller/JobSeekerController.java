package io.github.dafnipapado.careerstart_backend.controller;

import io.github.dafnipapado.careerstart_backend.core.exception.DataValidationException;
import io.github.dafnipapado.careerstart_backend.core.exception.EntityAlreadyExistsException;
import io.github.dafnipapado.careerstart_backend.core.exception.EntityNotFoundException;
import io.github.dafnipapado.careerstart_backend.core.exception.FileUploadException;
import io.github.dafnipapado.careerstart_backend.dto.attachment.AttachmentReadDTO;
import io.github.dafnipapado.careerstart_backend.dto.job_seeker.*;
import io.github.dafnipapado.careerstart_backend.filters.JobSeekerFilters;
import io.github.dafnipapado.careerstart_backend.service.IJobSeekerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @PostMapping(value = "/{uuid}/avatar-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadPictureFile(@PathVariable("uuid") UUID uuid, @RequestParam("picture") MultipartFile file)
            throws EntityNotFoundException, FileUploadException {

        jobSeekerService.uploadAttachment(uuid, file);

        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{uuid}/cv-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadCvFile(@PathVariable("uuid") UUID uuid, @RequestParam("cv") MultipartFile file)
            throws EntityNotFoundException, FileUploadException {

        jobSeekerService.uploadAttachment(uuid, file);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{uuid}/avatar")
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable("uuid") UUID uuid)
            throws EntityNotFoundException, IOException {
        AttachmentReadDTO attachmentReadDTO = jobSeekerService.getProfilePicture(uuid);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.parseMediaType(attachmentReadDTO.contentType()))
                .body(attachmentReadDTO.bytes());
    }

    @GetMapping("/{uuid}/cv")
    public ResponseEntity<byte[]> getCv(@PathVariable("uuid") UUID uuid)
            throws EntityNotFoundException, IOException {
        AttachmentReadDTO attachmentReadDTO = jobSeekerService.getCv(uuid);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.parseMediaType(attachmentReadDTO.contentType()))
                .body(attachmentReadDTO.bytes());
    }

    @GetMapping
    public ResponseEntity<Page<JobSeekerSummaryReadOnlyDTO>> getPaginatedFilteredJobSeekers(@ModelAttribute JobSeekerFilters jobSeekerFilters)
            throws EntityNotFoundException {
        Page<JobSeekerSummaryReadOnlyDTO> pagesDTO = jobSeekerService.getPaginatedFilteredJobSeekers(jobSeekerFilters);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pagesDTO);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<JobSeekerDetailsReadOnlyDTO> getCurrentJobSeeker() {
        JobSeekerDetailsReadOnlyDTO jobSeekerDetailsReadOnlyDTO = jobSeekerService.getCurrentJobSeeker();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(jobSeekerDetailsReadOnlyDTO);
    }

    @PostMapping("/{jobListingUuid}/apply")
    public ResponseEntity<Void> apply(@PathVariable("jobListingUuid") UUID jobListingUuid)
            throws EntityNotFoundException {
        jobSeekerService.apply(jobListingUuid);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{jobListingUuid}/withdraw")
    public ResponseEntity<Void> withdraw(@PathVariable("jobListingUuid") UUID jobListingUuid)
            throws EntityNotFoundException {
        jobSeekerService.withdraw(jobListingUuid);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{jobListingUuid}/has-applied")
    public ResponseEntity<Boolean> hasApplied(@PathVariable("jobListingUuid") UUID jobListingUuid)
            throws EntityNotFoundException {
        boolean hasJobSeekerApplied = jobSeekerService.hasJobListing(jobListingUuid);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(hasJobSeekerApplied);
    }
}
