package io.github.dafnipapado.careerstart_backend.core.exception;

import lombok.Getter;

@Getter
public class FileUploadException extends RuntimeException {
    private final String code;

    public FileUploadException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
