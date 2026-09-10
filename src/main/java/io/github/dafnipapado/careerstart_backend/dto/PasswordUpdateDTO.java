package io.github.dafnipapado.careerstart_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PasswordUpdateDTO(
        @NotBlank
        String oldPassword,

        @NotBlank
        @Pattern(regexp = "(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&+=])^.{8,}$")
        String newPassword
) {
}
