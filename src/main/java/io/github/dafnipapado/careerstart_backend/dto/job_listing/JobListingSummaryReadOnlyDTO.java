package io.github.dafnipapado.careerstart_backend.dto.job_listing;

public record JobListingSummaryReadOnlyDTO(
        String uuid,
        String title,
        String regionName,
        String professionalFieldName,
        String dateCreated,
        String employerUuid,
        String employerBrandName,
        boolean deleted
) {
}
