package io.github.dafnipapado.careerstart_backend.core.exception;

public class EntityNotFoundException extends GenericException {
    private static final String DEFAULT_CODE = "NotFound";

    public EntityNotFoundException(String code, String message) {
        super(code + DEFAULT_CODE, message);

    }


}
