package io.github.dafnipapado.careerstart_backend.service;

import io.github.dafnipapado.careerstart_backend.core.exception.EntityNotFoundException;
import io.github.dafnipapado.careerstart_backend.core.exception.InvalidCredentialsException;
import io.github.dafnipapado.careerstart_backend.dto.PasswordUpdateDTO;
import io.github.dafnipapado.careerstart_backend.model.User;
import io.github.dafnipapado.careerstart_backend.model.static_data.Role;

public interface IUserService {
    Role getRoleByName(String roleName) throws EntityNotFoundException;
    User getCurrentUser();
    User getCurrentUserByUuid() throws EntityNotFoundException;
    boolean verifyPassword(String password) throws EntityNotFoundException;
    void updatePassword(PasswordUpdateDTO passwordUpdateDTO) throws EntityNotFoundException, InvalidCredentialsException;
}
