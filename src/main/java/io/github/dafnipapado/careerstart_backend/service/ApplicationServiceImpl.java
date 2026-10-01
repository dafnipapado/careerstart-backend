package io.github.dafnipapado.careerstart_backend.service;

import io.github.dafnipapado.careerstart_backend.core.exception.EntityAlreadyExistsException;
import io.github.dafnipapado.careerstart_backend.core.exception.EntityNotFoundException;
import io.github.dafnipapado.careerstart_backend.enums.Status;
import io.github.dafnipapado.careerstart_backend.model.Application;
import io.github.dafnipapado.careerstart_backend.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationServiceImpl implements IApplicationService{

    private final ApplicationRepository applicationRepository;
    private final IUserService userService;

    @Override
    @Transactional(rollbackFor = {EntityNotFoundException.class, EntityAlreadyExistsException.class})
    public void accept(UUID jobSeekerUuid, UUID jobListingUuid) throws EntityNotFoundException, EntityAlreadyExistsException {
        Application application = getApplicationByJobSeekerUuidAndJobListingUuidDeletedFalse(jobSeekerUuid, jobListingUuid);
        if (application.getStatus().equals(Status.ACCEPTED)) throw new EntityAlreadyExistsException("ApplicationAccepted", "Application is already marked as accepted.");
        application.setStatus(Status.ACCEPTED);
        log.info("Applicant was accepted successfully.");
    }

    @Override
    @Transactional(rollbackFor = {EntityNotFoundException.class, EntityAlreadyExistsException.class})
    public void reject(UUID jobSeekerUuid, UUID jobListingUuid) throws EntityNotFoundException, EntityAlreadyExistsException {
        Application application = getApplicationByJobSeekerUuidAndJobListingUuidDeletedFalse(jobSeekerUuid, jobListingUuid);
        if (application.getStatus().equals(Status.REJECTED)) throw new EntityAlreadyExistsException("ApplicationRejected", "Application is already marked as rejected.");
        application.setStatus(Status.REJECTED);
        log.info("Applicant was rejected successfully.");
    }

    @Override
    public Application getApplicationByJobSeekerUuidAndJobListingUuid(UUID jobSeekerUuid, UUID jobListingUuid) throws EntityNotFoundException {
        return applicationRepository.findByJobSeeker_UuidAndJobListing_Uuid(jobSeekerUuid, jobListingUuid)
                .orElseThrow(() -> new EntityNotFoundException("Application", "Application not found"));
    }

    @Override
    public Application getApplicationByJobSeekerUuidAndJobListingUuidDeletedFalse(UUID jobSeekerUuid, UUID jobListingUuid) throws EntityNotFoundException {
        return applicationRepository.findByJobSeeker_UuidAndJobListing_UuidAndDeletedFalse(jobSeekerUuid, jobListingUuid)
                .orElseThrow(() -> new EntityNotFoundException("Application", "Active application not found"));
    }
}
