package io.github.dafnipapado.careerstart_backend.dto.job_seeker;

import io.github.dafnipapado.careerstart_backend.dto.personalInfo.PersonalInfoInsertDTO;
import io.github.dafnipapado.careerstart_backend.dto.user.UserInsertDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record JobSeekerInsertDTO(
    @NotBlank
    @Size(min = 3, max = 255)
    String firstname,

    @NotBlank
    @Size(min = 3, max = 255)
    String lastname,

    @NotNull
    @Valid
    UserInsertDTO userInsertDTO,

    @NotNull
    @Valid
    PersonalInfoInsertDTO personalInfoInsertDTO
) {
}
