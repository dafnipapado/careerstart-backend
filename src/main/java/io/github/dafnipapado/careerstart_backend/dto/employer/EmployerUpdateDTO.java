package io.github.dafnipapado.careerstart_backend.dto.employer;

import io.github.dafnipapado.careerstart_backend.dto.personalInfo.PersonalInfoUpdateDTO;
import io.github.dafnipapado.careerstart_backend.dto.user.UserUpdateDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record EmployerUpdateDTO(

        @NotNull
        UUID uuid,

        @NotBlank
        @Size(min = 2, max = 255)
        String brandName,

        @NotBlank
        @Pattern(regexp = "\\d{9,}")
        String vat,

        @Size(max = 255)
        String website,

        @NotBlank
        @Size(min = 20)
        String profile,

        @NotNull
        Long professionalFieldId,

        @NotNull
        @Valid
        UserUpdateDTO userUpdateDTO,

        @NotNull
        @Valid
        PersonalInfoUpdateDTO personalInfoUpdateDTO
) {
}
