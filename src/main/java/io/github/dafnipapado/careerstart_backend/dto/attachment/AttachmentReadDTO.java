package io.github.dafnipapado.careerstart_backend.dto.attachment;

public record AttachmentReadDTO(
        byte[] bytes,
        String contentType
) {
}
