package io.github.dafnipapado.careerstart_backend.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserInsertDTO(

        @NotBlank
        @Size(min = 3, max = 30)
        String username,

        @NotBlank
        @Pattern(regexp = "(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&+=])^.{8,}$")
        String password
) {
}
