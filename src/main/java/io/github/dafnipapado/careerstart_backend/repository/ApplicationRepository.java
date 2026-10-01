package io.github.dafnipapado.careerstart_backend.repository;

import io.github.dafnipapado.careerstart_backend.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Optional<Application> findByJobSeeker_UuidAndJobListing_Uuid(UUID jobSeekerUuid, UUID jobListingUuid);
    Optional<Application> findByJobSeeker_UuidAndJobListing_UuidAndDeletedFalse(UUID jobSeekerUuid, UUID jobListingUuid);
}
