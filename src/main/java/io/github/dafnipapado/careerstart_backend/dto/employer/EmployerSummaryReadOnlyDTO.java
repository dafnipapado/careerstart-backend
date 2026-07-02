package io.github.dafnipapado.careerstart_backend.dto.employer;

public record EmployerSummaryReadOnlyDTO(
        String uuid,
        String brandName,
        String website,
        String professionalFieldName,
        String regionName
) {
}
