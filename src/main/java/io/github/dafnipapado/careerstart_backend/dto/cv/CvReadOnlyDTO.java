package io.github.dafnipapado.careerstart_backend.dto.cv;

public record CvReadOnlyDTO(
        String uuid,
        String profession,
        String bio,
        String education,
        String experience,
        String certificates,
        String languages,
        String skills
) {
}
