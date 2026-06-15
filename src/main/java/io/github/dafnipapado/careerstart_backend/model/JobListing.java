package io.github.dafnipapado.careerstart_backend.model;

import io.github.dafnipapado.careerstart_backend.model.static_data.ProfessionalField;
import io.github.dafnipapado.careerstart_backend.model.static_data.Region;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "job_listings")
public class JobListing extends AbstractEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true, nullable = false, updatable = false)
    private UUID uuid;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "employer_id", nullable = false)
    private Employer employer;

    @ManyToOne
    @JoinColumn(name = "professional_field_id", nullable = false)
    private ProfessionalField professionalField;

    @ManyToOne
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;

    @ManyToMany(mappedBy = "jobListings")
    private Set<JobSeeker> jobSeekers = new HashSet<>();

    @PrePersist
    public void uuidInitialize() {
        this.uuid = UUID.randomUUID();
    }

    public void addJobSeeker(JobSeeker jobSeeker){
        jobSeekers.add(jobSeeker);
        jobSeeker.getJobListings().add(this);
    }

    public void removeJobSeeker(JobSeeker jobSeeker){
        jobSeekers.remove(jobSeeker);
        jobSeeker.getJobListings().remove(this);
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof JobListing that)) return false;
        return getUuid().equals(that.getUuid());
    }

    @Override
    public int hashCode() {
        return getUuid().hashCode();
    }
}
