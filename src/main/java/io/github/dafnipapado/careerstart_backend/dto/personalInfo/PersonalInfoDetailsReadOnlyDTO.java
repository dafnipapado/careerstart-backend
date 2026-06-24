package io.github.dafnipapado.careerstart_backend.dto.personalInfo;

public record PersonalInfoDetailsReadOnlyDTO(
        String email,
        String telephoneNumber,
        String address,
        Long regionId
) {
}
