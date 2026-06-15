package io.github.dafnipapado.careerstart_backend.model;

import io.github.dafnipapado.careerstart_backend.model.static_data.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends AbstractEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true, nullable = false, updatable = false)
    private UUID uuid;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @OneToOne(mappedBy = "user")
    private Employer employer;

    @OneToOne(mappedBy = "user")
    private JobSeeker jobSeeker;

    @PrePersist
    public void uuidInitialize() {
        this.uuid = UUID.randomUUID();
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof User user)) return false;
        return getUuid().equals(user.getUuid());
    }

    @Override
    public int hashCode() {
        return getUuid().hashCode();
    }
}
