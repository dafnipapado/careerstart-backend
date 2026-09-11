package io.github.dafnipapado.careerstart_backend.dto.employer;

import io.github.dafnipapado.careerstart_backend.dto.personalInfo.PersonalInfoDetailsReadOnlyDTO;

public record EmployerDetailsReadOnlyDTO(
        String uuid,
        String brandName,
        String vat,
        String website,
        String profile,
        String professionalFieldName,
        Long professionalFieldId,
        PersonalInfoDetailsReadOnlyDTO personalInfoDetailsReadOnlyDTO,
        boolean deleted
) {
}
