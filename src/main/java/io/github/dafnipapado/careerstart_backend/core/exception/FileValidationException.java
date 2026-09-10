package io.github.dafnipapado.careerstart_backend.core.exception;

import lombok.Getter;

@Getter
public class FileValidationException extends RuntimeException {
    private final String code;

    public FileValidationException(String code, String message) {
        super(message);
        this.code = code;
    }
}
