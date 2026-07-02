package io.github.dafnipapado.careerstart_backend.dto.job_listing;

import io.github.dafnipapado.careerstart_backend.dto.employer.EmployerSummaryReadOnlyDTO;

public record JobListingDetailsReadOnlyDTO(
        String uuid,
        String title,
        String description,
        EmployerSummaryReadOnlyDTO employerSummaryReadOnlyDTO
) {
}
