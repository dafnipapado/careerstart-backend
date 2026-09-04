package io.github.dafnipapado.careerstart_backend.service;

import io.github.dafnipapado.careerstart_backend.core.exception.EntityNotFoundException;
import io.github.dafnipapado.careerstart_backend.model.User;
import io.github.dafnipapado.careerstart_backend.model.static_data.Role;

public interface IUserService {
    Role getRoleByName(String roleName) throws EntityNotFoundException;
    User getCurrentUser();
    User getCurrentUserByUuid() throws EntityNotFoundException;
}
