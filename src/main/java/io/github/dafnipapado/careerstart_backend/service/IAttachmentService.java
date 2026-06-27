package io.github.dafnipapado.careerstart_backend.service;

import io.github.dafnipapado.careerstart_backend.core.exception.FileHandlingException;
import io.github.dafnipapado.careerstart_backend.dto.attachment.AttachmentUploadDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface IAttachmentService {
    AttachmentUploadDTO uploadAttachment(UUID actorUuid, MultipartFile file, String entity, String fileType) throws FileHandlingException;
}