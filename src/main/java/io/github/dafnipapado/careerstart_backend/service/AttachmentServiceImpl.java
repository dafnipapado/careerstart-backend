package io.github.dafnipapado.careerstart_backend.service;

import io.github.dafnipapado.careerstart_backend.core.exception.FileReadException;
import io.github.dafnipapado.careerstart_backend.core.exception.FileUploadException;
import io.github.dafnipapado.careerstart_backend.core.exception.FileValidationException;
import io.github.dafnipapado.careerstart_backend.dto.attachment.AttachmentReadDTO;
import io.github.dafnipapado.careerstart_backend.dto.attachment.AttachmentUploadDTO;
import io.github.dafnipapado.careerstart_backend.model.Attachment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttachmentServiceImpl implements IAttachmentService {

    @Value("${file.upload.directory}")
    private String uploadDirectory;

    private static final Set<String> ALLOWED_IMAGE_TYPES = Set.of(
            "image/jpeg",
            "image/jpg",
            "image/png"
    );
    private static final Set<String> ALLOWED_DOCUMENT_TYPES = Set.of(
            "application/pdf",
            "application/msword"
    );

    private final Tika tika;

    @Override
    public AttachmentUploadDTO uploadAttachment(UUID actorUuid, MultipartFile file, String entity, String type) throws FileUploadException {
        try {
            if (file.isEmpty()) {
                throw new FileValidationException("EmptyFile", "Uploaded file is empty");
            }

            String contentType = tika.detect(file.getBytes());
            if (type.equals("picture")) {
                if (!ALLOWED_IMAGE_TYPES.contains(contentType)) {
                    throw new FileValidationException("UnsupportedFileType", "Image content type is not allowed");
                }
            } else if (type.equals("document")) {
                if (!entity.equals("jobseeker") || !ALLOWED_DOCUMENT_TYPES.contains(contentType)) {
                    throw new FileValidationException("UnsupportedFileType", "Document content type is not allowed");
                }
            }
            else {
                throw new FileValidationException("UnsupportedFileType", "File content type is not allowed");
            }

            //generate saved name from uuid and original filename
            String originalFilename = file.getOriginalFilename();
            UUID uuid = UUID.randomUUID();
            String savedName = uuid.toString().substring(0, 8);
            if (!(originalFilename == null || originalFilename.isBlank())) {
                String filename = originalFilename.trim().replace(" ", "-").toLowerCase();
                savedName += "_" + filename;
            }

            //create the directory and filepath
            String directory = uploadDirectory + entity + "/" + actorUuid + "/" + type + "/";
            Path filePath = Paths.get(directory + savedName);
            Path directoryPath = Paths.get(directory);
            //-delete the already existing filepath, if present
            Path existingFilePath = null;
            if (Files.exists(directoryPath)) {
                try (Stream<Path> stream = Files.list(directoryPath)) {
                    existingFilePath = stream.findFirst().orElse(null);
                }
            }
            if (existingFilePath != null) Files.delete(existingFilePath);

            Files.createDirectories(filePath.getParent());
            file.transferTo(filePath);

            //get the file extension
            String extension = getFileExtension(originalFilename);

            return new AttachmentUploadDTO(uuid, originalFilename, savedName, filePath.toString(), contentType, extension, existingFilePath);
        } catch (IOException e) {
            throw new FileUploadException("FileUpload", "An unexpected error occurred during upload", e);
        }
    }

    @Override
    public AttachmentReadDTO getAttachmentData(Attachment attachment) {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(attachment.getFilepath()));
            String contentType = attachment.getContentType();
            String filename = attachment.getFilename();
            return new AttachmentReadDTO(bytes, contentType, filename);
        } catch (IOException e) {
            throw new FileReadException("FileRead", "Failed to read file", e);
        }
    }

    private String getFileExtension(String filename) {
        if (filename != null && filename.contains(".")) {
            return filename.substring(filename.lastIndexOf("."));
        }
        return "";
    }

}
