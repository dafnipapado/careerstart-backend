package io.github.dafnipapado.careerstart_backend.repository;

import io.github.dafnipapado.careerstart_backend.model.Employer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmployerRepository extends JpaRepository<Employer, Long> {
    Optional<Employer> findByUuid(UUID uuid);
    Optional<Employer> findByUuidAndDeletedFalse(UUID uuid);
    Optional<Employer> findByVat(String vat);

}
