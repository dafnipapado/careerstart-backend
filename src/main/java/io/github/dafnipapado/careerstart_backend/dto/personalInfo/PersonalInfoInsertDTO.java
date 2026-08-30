package io.github.dafnipapado.careerstart_backend.dto.personalInfo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record PersonalInfoInsertDTO(

        @NotBlank
        @Pattern(regexp = "\\w+\\.?\\w+@\\w+\\.\\w+")
        String email,

        String telephoneNumber,

        String address,

        @NotNull
        Long regionId
) {
}
