package io.github.dafnipapado.careerstart_backend.dto.job_listing;

public record JobListingReadOnlyDTO(
        String uuid,
        String title,
        String employerBrandName
) {
}
