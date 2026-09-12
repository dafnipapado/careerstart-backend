package io.github.dafnipapado.careerstart_backend.service;

import io.github.dafnipapado.careerstart_backend.core.exception.FileUploadException;
import io.github.dafnipapado.careerstart_backend.dto.attachment.AttachmentReadDTO;
import io.github.dafnipapado.careerstart_backend.dto.attachment.AttachmentUploadDTO;
import io.github.dafnipapado.careerstart_backend.model.Attachment;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface IAttachmentService {
    AttachmentUploadDTO uploadAttachment(UUID actorUuid, MultipartFile file, String entity, String type) throws FileUploadException;
    AttachmentReadDTO getAttachmentData(Attachment attachment);
}