package io.github.dafnipapado.careerstart_backend.service;

import io.github.dafnipapado.careerstart_backend.core.exception.FileHandlingException;
import io.github.dafnipapado.careerstart_backend.dto.attachment.AttachmentUploadDTO;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttachmentServiceImpl implements IAttachmentService {

    @Value("${file.upload.directory}")
    private String uploadDirectory;

    private final Tika tika;

    @Override
    public AttachmentUploadDTO uploadAttachment(UUID actorUuid, MultipartFile file, String entity, String fileType) throws FileHandlingException {
        try {
            //generate saved name from uuid and original filename
            String originalFilename = file.getOriginalFilename();
            UUID uuid = UUID.randomUUID();
            String savedName = uuid.toString().substring(0, 8);
            if (!(originalFilename == null || originalFilename.isBlank())) {
                String filename = originalFilename.trim().replace(" ", "-").toLowerCase();
                savedName += "_" + filename;
            }

            //create the directory and filepath
            String directory = uploadDirectory + entity + "/" + actorUuid + "/" + fileType + "/";
            Path filePath = Paths.get(directory + savedName);
            //-check if an attachment already exists in the directory
            String existingFilePath = "";
            if (Files.exists(filePath.getParent()) && !Files.list(filePath.getParent()).findAny().isEmpty()) {
                existingFilePath = filePath.toString();
                Files.delete(filePath);

            }
            Files.createDirectories(filePath.getParent());
            file.transferTo(filePath);

            //get the content type and extension of the file
            String contentType = tika.detect(file.getBytes());
            String extension = getFileExtension(originalFilename);

            AttachmentUploadDTO attachmentUploadDTO = new AttachmentUploadDTO(uuid, originalFilename, savedName, filePath.toString(), contentType, extension, existingFilePath);
            return attachmentUploadDTO;
        } catch (IOException e) {
            throw new FileHandlingException("FileHandlingError", "File could not be processed successfully.", e);
        }
    }

    private String getFileExtension(String filename) {
        if (filename != null && filename.contains(".")) {
            return filename.substring(filename.lastIndexOf("."));
        }
        return "";
    }

}
