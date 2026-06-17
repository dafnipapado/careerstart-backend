package io.github.dafnipapado.careerstart_backend.core.exception;

import lombok.Getter;

@Getter
public class GenericException extends Exception {
    private final String code;

    public GenericException(String code, String message) {
        super(message);
        this.code = code;
    }
}
