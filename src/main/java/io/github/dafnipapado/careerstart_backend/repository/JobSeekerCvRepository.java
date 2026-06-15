package io.github.dafnipapado.careerstart_backend.repository;

import io.github.dafnipapado.careerstart_backend.model.JobSeekerCv;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobSeekerCvRepository extends JpaRepository<JobSeekerCv, Long> {
}
