package io.github.dafnipapado.careerstart_backend.dto.job_seeker;

public record JobSeekerSummaryReadOnlyDTO(
        String uuid,
        String firstname,
        String lastname,
        String email
) {
}
