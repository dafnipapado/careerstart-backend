package io.github.dafnipapado.careerstart_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EmployerInsertDTO(

        @NotBlank
        @Size(min = 2, max = 255)
        String brandName,

        @NotBlank
        @Pattern(regexp = "\\d{9,}")
        String vat,

        @NotBlank
        @Size(max = 255)
        String website,

        @NotNull
        Long professionalFieldId,

        @NotNull
        UserInsertDTO userInsertDTO,

        @NotNull
        PersonalInfoInsertDTO personalInfoInsertDTO

) {
}
