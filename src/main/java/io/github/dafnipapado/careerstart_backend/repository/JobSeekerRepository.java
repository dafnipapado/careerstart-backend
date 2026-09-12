package io.github.dafnipapado.careerstart_backend.repository;

import io.github.dafnipapado.careerstart_backend.model.JobSeeker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface JobSeekerRepository extends JpaRepository<JobSeeker, Long>, JpaSpecificationExecutor<JobSeeker> {
    Optional<JobSeeker> findByUuid(UUID uuid);
    Optional<JobSeeker> findByUuidAndDeletedFalse(UUID uuid);
    Optional<JobSeeker> findByUuidAndDeletedTrue(UUID uuid);
}
