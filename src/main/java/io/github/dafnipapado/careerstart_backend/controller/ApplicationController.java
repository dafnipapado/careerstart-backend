package io.github.dafnipapado.careerstart_backend.controller;

import io.github.dafnipapado.careerstart_backend.core.exception.EntityAlreadyExistsException;
import io.github.dafnipapado.careerstart_backend.core.exception.EntityNotFoundException;
import io.github.dafnipapado.careerstart_backend.service.IApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/applications")
public class ApplicationController {

    private final IApplicationService applicationService;

    @PutMapping("/{jobListingUuid}/accept/{jobSeekerUuid}")
    public ResponseEntity<Void> accept(@PathVariable("jobListingUuid") UUID jobListingUuid, @PathVariable("jobSeekerUuid") UUID jobSeekerUuid)
            throws EntityNotFoundException, EntityAlreadyExistsException {
        applicationService.accept(jobSeekerUuid, jobListingUuid);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{jobListingUuid}/reject/{jobSeekerUuid}")
    public ResponseEntity<Void> reject(@PathVariable("jobListingUuid") UUID jobListingUuid, @PathVariable("jobSeekerUuid") UUID jobSeekerUuid)
            throws EntityNotFoundException, EntityAlreadyExistsException {
        applicationService.reject(jobSeekerUuid, jobListingUuid);
        return ResponseEntity.noContent().build();
    }
}
