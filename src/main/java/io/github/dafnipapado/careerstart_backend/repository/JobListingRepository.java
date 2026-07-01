package io.github.dafnipapado.careerstart_backend.repository;

import io.github.dafnipapado.careerstart_backend.model.JobListing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JobListingRepository extends JpaRepository<JobListing, Long> {
    Optional<JobListing> findByUuid(UUID uuid);
}
