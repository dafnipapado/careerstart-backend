package io.github.dafnipapado.careerstart_backend.controller;

import io.github.dafnipapado.careerstart_backend.core.exception.DataValidationException;
import io.github.dafnipapado.careerstart_backend.core.exception.EntityAlreadyExistsException;
import io.github.dafnipapado.careerstart_backend.core.exception.EntityNotFoundException;
import io.github.dafnipapado.careerstart_backend.dto.attachment.AttachmentReadDTO;
import io.github.dafnipapado.careerstart_backend.dto.employer.*;
import io.github.dafnipapado.careerstart_backend.filters.EmployerFilters;
import io.github.dafnipapado.careerstart_backend.service.IEmployerService;
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

    @PatchMapping(value = "/{uuid}/activate")
    public ResponseEntity<EmployerReadOnlyDTO> activate(@PathVariable("uuid") UUID uuid)
            throws EntityNotFoundException {

        employerService.activate(uuid);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{uuid}/view")
    public ResponseEntity<EmployerDetailsReadOnlyDTO> getSingleEmployer(@PathVariable("uuid") UUID uuid)
            throws EntityNotFoundException {

        EmployerDetailsReadOnlyDTO employerDetailsReadOnlyDTO = employerService.getSingleEmployer(uuid);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(employerDetailsReadOnlyDTO);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<EmployerDetailsReadOnlyDTO> getSingleEmployerDeletedFalse(@PathVariable UUID uuid)
            throws EntityNotFoundException {

        EmployerDetailsReadOnlyDTO employerDetailsReadOnlyDTO = employerService.getSingleEmployerDeletedFalse(uuid);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(employerDetailsReadOnlyDTO);
    }

    @PostMapping(value = "/{uuid}/avatar-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadPicture(@PathVariable UUID uuid, @RequestParam("picture") MultipartFile file)
            throws EntityNotFoundException, IOException {

        employerService.uploadAttachment(uuid, file);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{uuid}/avatar")
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable("uuid") UUID uuid)
            throws EntityNotFoundException, IOException {
        AttachmentReadDTO attachmentReadDTO = employerService.getProfilePicture(uuid);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.parseMediaType(attachmentReadDTO.contentType()))
                .body(attachmentReadDTO.bytes());
    }

    @GetMapping
    public ResponseEntity<Page<EmployerDetailsReadOnlyDTO>> getPaginatedFilteredEmployers(@ModelAttribute EmployerFilters employerFilters)
            throws EntityNotFoundException {

        Page<EmployerDetailsReadOnlyDTO> pageDTO = employerService.getPaginatedFilteredEmployers(employerFilters);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pageDTO);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<EmployerDetailsReadOnlyDTO> getCurrentEmployer() {
        EmployerDetailsReadOnlyDTO employerDetailsReadOnlyDTO = employerService.getCurrentEmployer();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(employerDetailsReadOnlyDTO);
    }

    @GetMapping("/count-job-listings")
    public ResponseEntity<Long> countEmployerJobListings() {
        long jobListingsNumber = employerService.countEmployerJobListings();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(jobListingsNumber);
    }

    @GetMapping("/{uuid}/count-job-listings")
    public ResponseEntity<Long> countEmployerJobListings(@PathVariable("uuid") UUID uuid) throws EntityNotFoundException{
        long jobListingsNumber = employerService.countEmployerJobListingsByEmployerUuid(uuid);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(jobListingsNumber);
    }
}
