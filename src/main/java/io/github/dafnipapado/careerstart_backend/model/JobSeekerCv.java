package io.github.dafnipapado.careerstart_backend.model;

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
@Table(name = "job_seeker_cvs")
public class JobSeekerCv {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true, nullable = false,updatable = false)
    UUID uuid;

    @Column(nullable = false)
    private String profession;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(columnDefinition = "TEXT")
    private String education;

    @Column(columnDefinition = "TEXT")
    private String experience;

    @Column(columnDefinition = "TEXT")
    private String certificates;

    @Column(columnDefinition = "TEXT")
    private String languages;

    @Column(columnDefinition = "TEXT")
    private String skills;

    @OneToOne
    @JoinColumn(name = "job_seeker_id", nullable = false)
    private JobSeeker jobSeeker;

    @PrePersist
    public void uuidInitialize() {
        this.uuid = UUID.randomUUID();
    }

}
