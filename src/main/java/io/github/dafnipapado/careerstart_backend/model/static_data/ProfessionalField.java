package io.github.dafnipapado.careerstart_backend.model.static_data;

import io.github.dafnipapado.careerstart_backend.model.Employer;
import io.github.dafnipapado.careerstart_backend.model.JobListing;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "professional_fields")
public class ProfessionalField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "professionalField")
    private Set<JobListing> jobListings = new HashSet<>();

    @OneToMany(mappedBy = "professionalField")
    private Set<Employer> employers = new HashSet<>();

    public void addJobListing(JobListing jobListing) {
        jobListings.add(jobListing);
        jobListing.setProfessionalField(this);
    }

    public void removeJobListing(JobListing jobListing) {
        jobListings.remove(jobListing);
    }

    public void addEmployer(Employer employer){
        employers.add(employer);
        employer.setProfessionalField(this);
    }

    public void removeEmployer(Employer employer){
        employers.remove(employer);
        employer.setProfessionalField(null);
    }


}
