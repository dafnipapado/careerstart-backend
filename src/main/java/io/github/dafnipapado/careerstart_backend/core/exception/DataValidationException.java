package io.github.dafnipapado.careerstart_backend.core.exception;

import lombok.Getter;
import org.springframework.validation.BindingResult;

@Getter
public class DataValidationException extends GenericException {
    private final BindingResult bindingResult;
    private static final String DEFAULT_CODE = "InvalidData";

    public DataValidationException(String code, String message, BindingResult bindingResult) {
        super(code + DEFAULT_CODE, message);
        this.bindingResult = bindingResult;
    }
}
