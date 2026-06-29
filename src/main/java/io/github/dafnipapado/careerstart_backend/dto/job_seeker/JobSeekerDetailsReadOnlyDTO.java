package io.github.dafnipapado.careerstart_backend.dto.job_seeker;

import io.github.dafnipapado.careerstart_backend.dto.personalInfo.PersonalInfoDetailsReadOnlyDTO;

public record JobSeekerDetailsReadOnlyDTO(
        String uuid,
        String firstname,
        String lastname,
        PersonalInfoDetailsReadOnlyDTO personalInfoDetailsReadOnlyDTO
) {
}
