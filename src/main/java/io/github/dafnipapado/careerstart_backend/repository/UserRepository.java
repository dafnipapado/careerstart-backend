package io.github.dafnipapado.careerstart_backend.repository;

import io.github.dafnipapado.careerstart_backend.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = {"role", "role.capabilities"})
    Optional<User> findByUsername(String username);
    Optional<User> findByRoleId(Long id);

}
