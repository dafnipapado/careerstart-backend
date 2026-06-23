package io.github.dafnipapado.careerstart_backend.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateDTO(

        @NotBlank
        @Size(min = 3, max = 30)
        String username
) {
}
