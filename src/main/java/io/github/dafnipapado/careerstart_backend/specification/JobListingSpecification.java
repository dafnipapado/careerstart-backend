package io.github.dafnipapado.careerstart_backend.specification;

import io.github.dafnipapado.careerstart_backend.filters.JobListingFilters;
import io.github.dafnipapado.careerstart_backend.model.JobListing;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.UUID;

public class JobListingSpecification {

    public static Specification<JobListing> build(JobListingFilters jobListingFilters) {
        return Specification.allOf(
                hasTitle(jobListingFilters.getTitle()),
                hasRegion(jobListingFilters.getRegionId()),
                hasProfessionalField(jobListingFilters.getProfessionalFieldId()),
                hasDateCreated(jobListingFilters.getCreatedAt()),
                hasEmployerBrandName(jobListingFilters.getEmployerBrandName()),
                hasEmployerUuid(jobListingFilters.getEmployerUuid()),
                isDeleted(jobListingFilters.isDeleted()),
                hasJobSeekerUuid(jobListingFilters.getJobSeekerUuid())
        );
    }

    private static Specification<JobListing> hasTitle(String title) {
        return (root, query, cb) -> title == null
                ? cb.conjunction()
                : cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    private static Specification<JobListing> hasRegion(Long regionId) {
        return (root, query, cb) -> regionId == null
                ? cb.conjunction()
                : cb.equal(root.get("region").get("id"), regionId);
    }

    private static Specification<JobListing> hasProfessionalField(Long professionalFieldId) {
        return (root, query, cb) -> professionalFieldId == null
                ? cb.conjunction()
                : cb.equal(root.get("professionalField").get("id"), professionalFieldId);
    }

    private static Specification<JobListing> hasDateCreated(LocalDate createdAt) {
        return (root, query, cb) -> createdAt == null
                ? cb.conjunction()
                : cb.greaterThanOrEqualTo(root.get("createdAt"), createdAt.atStartOfDay().toInstant(ZoneOffset.UTC));
    }

    private static Specification<JobListing> hasEmployerBrandName(String employerBrandName) {
        return (root, query, cb) -> employerBrandName == null
                ? cb.conjunction()
                : cb.like(cb.lower(root.get("employer").get("brandName")), "%" + employerBrandName + "%");
    }

    private static Specification<JobListing> hasEmployerUuid(UUID employerUuid) {
        return (root, query, cb) -> employerUuid == null
                ? cb.conjunction()
                : cb.equal(root.get("employer").get("uuid"), employerUuid);
    }

    private static Specification<JobListing> isDeleted(boolean deleted) {
        return (root, query, cb) ->
                cb.equal(root.get("deleted"), deleted);
    }

    private static Specification<JobListing> hasJobSeekerUuid(UUID jobSeekerUuid) {
        return (root, query, cb) -> {
            if (query != null) query.distinct(true);
            return jobSeekerUuid == null
                    ? cb.conjunction()
                    : cb.equal(root.join("jobSeekers").get("uuid"), jobSeekerUuid);
        };
    }
}