package io.github.dafnipapado.careerstart_backend.dto.employer;

import io.github.dafnipapado.careerstart_backend.dto.personalInfo.PersonalInfoDetailsReadOnlyDTO;

public record EmployerDetailsReadOnlyDTO(
        String uuid,
        String brandName,
        String website,
        Long professionalFieldId,
        PersonalInfoDetailsReadOnlyDTO personalInfoDetailsReadOnlyDTO
) {
}
