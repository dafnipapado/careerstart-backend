package io.github.dafnipapado.careerstart_backend.dto.personalInfo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record PersonalInfoInsertDTO(

        @NotBlank
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        String email,

        String telephoneNumber,

        String address,

        @NotNull
        Long regionId
) {
}
