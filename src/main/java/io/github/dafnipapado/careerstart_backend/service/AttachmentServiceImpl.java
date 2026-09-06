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
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttachmentServiceImpl implements IAttachmentService {

    @Value("${file.upload.directory}")
    private String uploadDirectory;

    private final Tika tika;

    @Override
    public AttachmentUploadDTO uploadAttachment(UUID actorUuid, MultipartFile file, String entity) throws FileHandlingException {
        try {
            //generate saved name from uuid and original filename
            String originalFilename = file.getOriginalFilename();
            UUID uuid = UUID.randomUUID();
            String savedName = uuid.toString().substring(0, 8);
            if (!(originalFilename == null || originalFilename.isBlank())) {
                String filename = originalFilename.trim().replace(" ", "-").toLowerCase();
                savedName += "_" + filename;
            }

            //get the file's contentType with tika
            String fileType = "";
            String contentType = tika.detect(file.getBytes());
            if (contentType.startsWith("image")) {
                fileType = "picture";
            } else {
                fileType = "document";
            }

            //create the directory and filepath
            String directory = uploadDirectory + entity + "/" + actorUuid + "/" + fileType + "/";
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
