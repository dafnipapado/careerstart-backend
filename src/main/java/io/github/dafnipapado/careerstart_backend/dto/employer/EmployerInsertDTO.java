package io.github.dafnipapado.careerstart_backend.dto.employer;

import io.github.dafnipapado.careerstart_backend.dto.personalInfo.PersonalInfoInsertDTO;
import io.github.dafnipapado.careerstart_backend.dto.user.UserInsertDTO;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
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

        @Nullable
        @Size(max = 255)
        String website,

        @NotNull
        Long professionalFieldId,

        @NotNull
        @Valid
        UserInsertDTO userInsertDTO,

        @NotNull
        @Valid
        PersonalInfoInsertDTO personalInfoInsertDTO

) {
}
