package io.github.dafnipapado.careerstart_backend.repository;

import io.github.dafnipapado.careerstart_backend.model.JobListing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface JobListingRepository extends JpaRepository<JobListing, Long>, JpaSpecificationExecutor<JobListing> {
    Optional<JobListing> findByUuid(UUID uuid);
    Optional<JobListing> findByUuidAndDeletedFalse(UUID uuid);
    long countByEmployerUuidAndDeletedFalse(UUID employerUuid);
}
