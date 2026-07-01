package io.github.dafnipapado.careerstart_backend.dto.job_listing;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record JobListingUpdateDTO(
        @NotNull
        UUID uuid,

        @NotBlank
        @Size(min = 3, max = 255)
        String title,

        @Size(min = 20)
        String description,

        @NotNull
        Long professionalFieldId,

        @NotNull
        Long regionId
) {
}
