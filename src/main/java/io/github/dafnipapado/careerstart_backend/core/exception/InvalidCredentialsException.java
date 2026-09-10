package io.github.dafnipapado.careerstart_backend.core.exception;

public class InvalidCredentialsException extends GenericException {
    private static final String DEFAULT_CODE = "InvalidCredentials";

    public InvalidCredentialsException(String code, String message) {
        super(code + DEFAULT_CODE, message);
    }
}
