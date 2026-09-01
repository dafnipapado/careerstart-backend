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
            hasRegion(jobListingFilters.getRegion()),
            hasProfessionalField(jobListingFilters.getProfessionalField()),
            hasDateCreated(jobListingFilters.getCreatedAt()),
            hasEmployerBrandName(jobListingFilters.getEmployerBrandName()),
            hasEmployerUuid(jobListingFilters.getEmployerUuid())
        );
    }

    private static Specification<JobListing> hasTitle(String title) {
        return (root, query, cb) -> title == null
                ? cb.conjunction()
                : cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    private static Specification<JobListing> hasRegion(String region) {
        return (root, query, cb) -> region == null
                ? cb.conjunction()
                : cb.equal(cb.lower(root.get("region").get("name")), region.toLowerCase());
    }

    private static Specification<JobListing> hasProfessionalField(String professionalField) {
        return (root, query, cb) -> professionalField == null
                ? cb.conjunction()
                : cb.equal(cb.lower(root.get("professionalField").get("name")), professionalField.toLowerCase());
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
        ?cb.conjunction()
        : cb.equal(root.get("employer").get("uuid"), employerUuid);
    }
}
