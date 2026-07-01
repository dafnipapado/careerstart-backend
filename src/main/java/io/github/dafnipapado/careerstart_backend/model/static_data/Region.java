package io.github.dafnipapado.careerstart_backend.model.static_data;

import io.github.dafnipapado.careerstart_backend.model.JobListing;
import io.github.dafnipapado.careerstart_backend.model.PersonalInfo;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "regions")
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "region")
    private Set<JobListing> jobListings = new HashSet<>();

    @OneToMany(mappedBy = "region")
    private Set<PersonalInfo> personalInfos = new HashSet<>();

    public void addJobListing(JobListing jobListing) {
        jobListings.add(jobListing);
        jobListing.setRegion(this);
    }

    public void removeJobListing(JobListing jobListing){
        jobListings.remove(jobListing);
    }

    public void addPersonalInfo(PersonalInfo personalInfo){
        personalInfos.add(personalInfo);
        personalInfo.setRegion(this);
    }

    public void removePersonalInfo(PersonalInfo personalInfo){
        personalInfos.remove(personalInfo);
        personalInfo.setRegion(null);
    }

}
