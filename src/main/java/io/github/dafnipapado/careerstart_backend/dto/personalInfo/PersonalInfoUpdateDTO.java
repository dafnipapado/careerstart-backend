package io.github.dafnipapado.careerstart_backend.dto.personalInfo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record PersonalInfoUpdateDTO(

        @NotBlank
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        String email,

        @Pattern(regexp = "\\d{10}")
        String telephoneNumber,

        String address,

        @NotNull
        Long regionId
) {
}
