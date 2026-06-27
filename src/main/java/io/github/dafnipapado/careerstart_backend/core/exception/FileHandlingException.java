package io.github.dafnipapado.careerstart_backend.core.exception;

import lombok.Getter;

import java.io.IOException;

@Getter
public class FileHandlingException extends IOException {
    private final String code;

    public FileHandlingException(String code, String message, Throwable cause) {
        super(message);
        this.code = code;
    }
}
