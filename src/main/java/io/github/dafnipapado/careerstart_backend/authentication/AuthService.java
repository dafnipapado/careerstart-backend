package io.github.dafnipapado.careerstart_backend.authentication;

import io.github.dafnipapado.careerstart_backend.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO dto) {
        Map<String, Object> roleClaims = new HashMap<>();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.username(), dto.password())
        );
        User user = (User) authentication.getPrincipal();
        roleClaims.put("role", user.getRole().getName());

        String token = jwtService.generateToken(user, roleClaims);

        return new AuthenticationResponseDTO(token);
    }
}
