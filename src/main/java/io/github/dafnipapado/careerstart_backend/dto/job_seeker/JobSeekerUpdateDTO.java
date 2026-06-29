package io.github.dafnipapado.careerstart_backend.dto.job_seeker;

import io.github.dafnipapado.careerstart_backend.dto.personalInfo.PersonalInfoUpdateDTO;
import io.github.dafnipapado.careerstart_backend.dto.user.UserUpdateDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record JobSeekerUpdateDTO(
        @NotNull
        UUID uuid,

        @NotBlank
        @Size(min = 3, max = 255)
        String firstname,

        @NotBlank
        @Size(min = 3, max = 255)
        String lastname,

        @NotNull
        @Valid
        UserUpdateDTO userUpdateDTO,

        @NotNull
        @Valid
        PersonalInfoUpdateDTO personalInfoUpdateDTO
) {
}
