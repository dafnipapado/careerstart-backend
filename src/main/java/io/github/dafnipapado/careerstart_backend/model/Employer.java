package io.github.dafnipapado.careerstart_backend.model;

import io.github.dafnipapado.careerstart_backend.model.static_data.ProfessionalField;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table (name = "employers")
public class Employer extends AbstractEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true, nullable = false, updatable = false)
    private UUID uuid;

    @Column(name = "brand_name", nullable = false)
    private String brandName;

    @Column(unique = true, nullable = false)
    private String vat;

    @Column
    private String website;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String profile;

    @ManyToOne
    @JoinColumn(name = "professional_field_id", nullable = false)
    private ProfessionalField professionalField;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "personal_info_id", nullable = false)
    private PersonalInfo personalInfo;

    @OneToMany(mappedBy = "employer")
    private Set<JobListing> jobListings = new HashSet<>();

    @PrePersist
    public void uuidInitialize() {
        this.uuid = UUID.randomUUID();
    }

    public void addJobListing(JobListing jobListing){
        jobListings.add(jobListing);
        jobListing.setEmployer(this);
    }

    public void removeJobListing(JobListing jobListing){
        jobListings.remove(jobListing);
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Employer employer)) return false;
        return getUuid().equals(employer.getUuid());
    }

    @Override
    public int hashCode() {
        return getUuid().hashCode();
    }
}
