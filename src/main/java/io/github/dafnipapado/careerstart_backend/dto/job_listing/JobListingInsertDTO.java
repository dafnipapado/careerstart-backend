package io.github.dafnipapado.careerstart_backend.dto.job_listing;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record JobListingInsertDTO(
    @NotBlank
    @Size(min = 3, max = 255)
    String title,

    @NotBlank
    @Size(min = 20)
    String description,

    @NotNull
    Long employerId,

    @NotNull
    Long professionalFieldId,

    @NotNull
    Long regionId
) {
}
