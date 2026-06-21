package io.github.dafnipapado.careerstart_backend.dto.error;

import java.util.Map;

public record ValidationErrorResponseDTO(
        String code,
        String message,
        Map<String, String> errors
) {
}
