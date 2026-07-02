package io.github.dafnipapado.careerstart_backend.specification;

import io.github.dafnipapado.careerstart_backend.filters.EmployerFilters;
import io.github.dafnipapado.careerstart_backend.model.Employer;
import org.springframework.data.jpa.domain.Specification;


public class EmployerSpecification {

    public static Specification<Employer> build(EmployerFilters employerFilters){
        return Specification.allOf(
                hasBrandName(employerFilters.getBrandName()),
                hasProfessionalField(employerFilters.getProfessionalField()),
                hasRegion(employerFilters.getRegion()),
                isDeleted(employerFilters.isDeleted())
        );
    }

    private static Specification<Employer> hasBrandName(String brandName) {
        return (root, query, cb) -> brandName == null
                ? cb.conjunction()
                : cb.like(cb.lower(root.get("brandName")), "%" + brandName.toLowerCase() + "%");
    }

    private static Specification<Employer> hasProfessionalField(String professionalField) {
        return (root, query, cb) -> professionalField == null
                ? cb.conjunction()
                : cb.equal(cb.lower(root.get("professionalField").get("name")), professionalField.toLowerCase());
    }

    private static Specification<Employer> hasRegion(String region) {
        return (root, query, cb) -> region == null
                ? cb.conjunction()
                : cb.equal(cb.lower(root.get("region").get("name")), region.toLowerCase());
    }

    private static Specification<Employer> isDeleted(boolean deleted) {
        return (root, query, cb) ->
                cb.equal(root.get("deleted"), deleted);
    }
}
