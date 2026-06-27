package io.github.dafnipapado.careerstart_backend.core.exception;

import lombok.Getter;

import java.io.IOException;

@Getter
public class FileUploadException extends IOException {
    private final String code;
    private static final String DEFAULT_CODE = "FileUploadError";

    public FileUploadException(String code, String message, Throwable cause) {
        super(message);
        this.code = code + DEFAULT_CODE;
    }
}
