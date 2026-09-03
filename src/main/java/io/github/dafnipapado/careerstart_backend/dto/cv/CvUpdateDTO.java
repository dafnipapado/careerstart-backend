package io.github.dafnipapado.careerstart_backend.dto.cv;

import java.util.UUID;

public record CvUpdateDTO(
        UUID uuid,
        String profession,
        String bio,
        String education,
        String experience,
        String certificates,
        String languages,
        String skills
) {
}
