package io.github.dafnipapado.careerstart_backend.specification;

import io.github.dafnipapado.careerstart_backend.filters.JobSeekerFilters;
import io.github.dafnipapado.careerstart_backend.model.JobSeeker;
import org.springframework.data.jpa.domain.Specification;

public class JobSeekerSpecification {

    public static Specification<JobSeeker> build(JobSeekerFilters jobSeekerFilters) {
        return Specification.allOf(
                hasLastname(jobSeekerFilters.getLastname()),
                hasRegion(jobSeekerFilters.getRegion())
        );
    }

    private static Specification<JobSeeker> hasLastname(String lastname) {
        return (root, query, cb) -> lastname == null
                ? cb.conjunction()
                : cb.like(cb.lower(root.get("lastname")), lastname.toLowerCase() + "%");
    }

    private static Specification<JobSeeker> hasRegion(String region) {
        return (root, query, cb) -> region == null
                ? cb.conjunction()
                : cb.equal(cb.lower(root.get("region").get("name")), region.toLowerCase());
    }
}
