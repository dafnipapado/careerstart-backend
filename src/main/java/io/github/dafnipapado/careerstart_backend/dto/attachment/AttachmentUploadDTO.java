package io.github.dafnipapado.careerstart_backend.dto.attachment;

import java.util.UUID;

public record AttachmentUploadDTO(
        UUID uuid,
        String filename,
        String savedName,
        String filepath,
        String contentType,
        String extension,
        String existingFilePath
) {
}
