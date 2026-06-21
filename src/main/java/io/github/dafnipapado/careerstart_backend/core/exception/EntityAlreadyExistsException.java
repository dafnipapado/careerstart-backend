package io.github.dafnipapado.careerstart_backend.core.exception;

public class EntityAlreadyExistsException extends GenericException {
    private static final String DEFAULT_CODE = "EntityAlreadyExists";

    public EntityAlreadyExistsException(String code, String message) {
        super(DEFAULT_CODE + code, message);
    }
}
