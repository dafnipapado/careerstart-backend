package io.github.dafnipapado.careerstart_backend.service;

import io.github.dafnipapado.careerstart_backend.core.exception.EntityNotFoundException;
import io.github.dafnipapado.careerstart_backend.model.User;
import io.github.dafnipapado.careerstart_backend.model.static_data.Role;
import io.github.dafnipapado.careerstart_backend.repository.RoleRepository;
import io.github.dafnipapado.careerstart_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements IUserService{

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Override
    public Role getRoleByName(String roleName) throws EntityNotFoundException {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new EntityNotFoundException("Role", "Role with name = {" + roleName + "} not found."));
    }

    @Override
    public User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public User getCurrentUserByUuid() throws EntityNotFoundException {
        UUID uuid = getCurrentUser().getUuid();
        return userRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("User", "User with uuid = {" + uuid + "} not found."));
    }
}
