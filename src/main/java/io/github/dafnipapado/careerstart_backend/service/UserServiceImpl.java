package io.github.dafnipapado.careerstart_backend.service;

import io.github.dafnipapado.careerstart_backend.core.exception.EntityNotFoundException;
import io.github.dafnipapado.careerstart_backend.core.exception.InvalidCredentialsException;
import io.github.dafnipapado.careerstart_backend.dto.PasswordUpdateDTO;
import io.github.dafnipapado.careerstart_backend.model.User;
import io.github.dafnipapado.careerstart_backend.model.static_data.Role;
import io.github.dafnipapado.careerstart_backend.repository.RoleRepository;
import io.github.dafnipapado.careerstart_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements IUserService{

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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

    @Override
    public boolean verifyPassword(String oldPassword) throws EntityNotFoundException {
        return passwordEncoder.matches(oldPassword, getCurrentUserByUuid().getPassword());
    }

    @Override
    @Transactional(rollbackFor = {EntityNotFoundException.class, InvalidCredentialsException.class})
    public void updatePassword(PasswordUpdateDTO passwordUpdateDTO) throws EntityNotFoundException, InvalidCredentialsException {
        User user = getCurrentUserByUuid();
        if (!verifyPassword(passwordUpdateDTO.oldPassword())) throw new InvalidCredentialsException("User", "Wrong password during update");
        user.setPassword(passwordEncoder.encode(passwordUpdateDTO.newPassword()));
    }
}
