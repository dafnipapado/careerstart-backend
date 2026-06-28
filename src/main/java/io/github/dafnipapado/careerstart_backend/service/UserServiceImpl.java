package io.github.dafnipapado.careerstart_backend.service;

import io.github.dafnipapado.careerstart_backend.core.exception.EntityNotFoundException;
import io.github.dafnipapado.careerstart_backend.model.static_data.Role;
import io.github.dafnipapado.careerstart_backend.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements IUserService{

    private final RoleRepository roleRepository;

    @Override
    public Role getRoleByName(String roleName) throws EntityNotFoundException {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new EntityNotFoundException("Role", "Role with id = {" + roleName + "} not found."));
    }
}
