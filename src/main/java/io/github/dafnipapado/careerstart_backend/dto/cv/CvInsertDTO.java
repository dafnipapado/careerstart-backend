package io.github.dafnipapado.careerstart_backend.dto.cv;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CvInsertDTO(
        @NotBlank
        @Size(min = 3, max = 255)
        String profession,

        @Nullable
        String bio,

        @Nullable
        String education,

        @Nullable
        String experience,

        @Nullable
        String certificates,

        @Nullable
        String languages,

        @Nullable
        String skills
) {
}
