package io.github.dafnipapado.careerstart_backend.repository;

import io.github.dafnipapado.careerstart_backend.model.JobSeeker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobSeekerRepository extends JpaRepository<JobSeeker, Long> {
}
