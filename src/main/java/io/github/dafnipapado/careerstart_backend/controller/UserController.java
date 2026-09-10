package io.github.dafnipapado.careerstart_backend.controller;

import io.github.dafnipapado.careerstart_backend.core.exception.DataValidationException;
import io.github.dafnipapado.careerstart_backend.core.exception.EntityNotFoundException;
import io.github.dafnipapado.careerstart_backend.core.exception.InvalidCredentialsException;
import io.github.dafnipapado.careerstart_backend.dto.PasswordUpdateDTO;
import io.github.dafnipapado.careerstart_backend.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final IUserService userService;

    @PatchMapping("/update-password")
    public ResponseEntity<Void> updatePassword(@Valid @RequestBody PasswordUpdateDTO passwordUpdateDTO, BindingResult bindingResult)
            throws EntityNotFoundException, InvalidCredentialsException, DataValidationException {

        if (bindingResult.hasErrors()) {
            throw new DataValidationException("User", "Password validation failed during update.", bindingResult);
        }
        userService.updatePassword(passwordUpdateDTO);
        return ResponseEntity.noContent().build();
    }

}
