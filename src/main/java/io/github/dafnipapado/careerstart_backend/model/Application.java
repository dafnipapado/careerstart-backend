package io.github.dafnipapado.careerstart_backend.model;

import io.github.dafnipapado.careerstart_backend.enums.Status;
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
@Table(name = "applications")
public class Application extends AbstractEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true, nullable = false,updatable = false)
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "job_seeker_id", nullable = false)
    private JobSeeker jobSeeker;

    @ManyToOne
    @JoinColumn(name = "job_listing_id", nullable = false)
    private JobListing jobListing;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @PrePersist
    private void uuidInitialize() {
        this.uuid = UUID.randomUUID();
    }
}
