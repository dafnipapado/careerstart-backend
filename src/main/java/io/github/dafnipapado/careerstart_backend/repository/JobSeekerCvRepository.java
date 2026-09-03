package io.github.dafnipapado.careerstart_backend.repository;

import io.github.dafnipapado.careerstart_backend.model.JobSeekerCv;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JobSeekerCvRepository extends JpaRepository<JobSeekerCv, Long> {
    Optional<JobSeekerCv> findByUuid(UUID uuid);
}
