package io.github.dafnipapado.careerstart_backend.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.apachecommons.CommonsLog;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "job_seekers")
public class JobSeeker extends AbstractEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true, nullable = false,updatable = false)
    private UUID uuid;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "personal_info_id", nullable = false)
    private PersonalInfo personalInfo;

    @ManyToMany
    @JoinTable(
        name = "job_seekers_job_listings",
        joinColumns = @JoinColumn(name = "job_seeker_id"),
        inverseJoinColumns = @JoinColumn(name = "job_listing_id")
    )
    private Set<JobListing> jobListings = new HashSet<>();

    @OneToOne(mappedBy = "jobSeeker", orphanRemoval = true)
    private JobSeekerCv jobSeekerCv;

    @PrePersist
    public void uuidInitialize() {
        this.uuid = UUID.randomUUID();
    }

    public void addJobListing(JobListing jobListing){
        jobListings.add(jobListing);
        jobListing.getJobSeekers().add(this);
    }

    public void removeJobListing(JobListing jobListing){
        jobListings.remove(jobListing);
        jobListing.getJobSeekers().remove(this);
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof JobSeeker jobSeeker)) return false;
        return getUuid().equals(jobSeeker.getUuid());
    }

    @Override
    public int hashCode() {
        return getUuid().hashCode();
    }
}
