package eventy.services;

import eventy.domain.errors.DomainError;
import eventy.domain.errors.FailedModificationError;
import eventy.services.interfaces.UploadService;
import io.vavr.control.Either;
import io.vavr.control.Option;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service
public class UploadServiceImpl implements UploadService {
    @Override
    public Either<DomainError, String> upload(MultipartFile file, String folder) {
        try {
            if (file.isEmpty()) {
                return Either.left(new DomainError("Error uploading file. File is empty."));
            }

            String originalFilename = file.getOriginalFilename();
            String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            String newFilename = folder + "/" + UUID.randomUUID().toString().concat(".").concat(ext);
            Path root = Paths.get("uploads");
            Path destFile = root.resolve(Paths.get(Objects.requireNonNull(newFilename))).normalize().toAbsolutePath();

            if (!destFile.getParent().startsWith(root.toAbsolutePath())) {
                return Either.left(new DomainError("Cannot store file outside current directory"));
            }

            try (InputStream stream = file.getInputStream()) {
                Files.copy(stream, destFile, StandardCopyOption.REPLACE_EXISTING);
            }

            return Either.right(newFilename);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return Either.left(new DomainError(e.getMessage()));
        }
    }

    @Override
    public Option<DomainError> delete(String resource) {
        try {
            Path root = Paths.get("uploads");
            Path file = root.resolve(resource);
            Files.deleteIfExists(file);
            return Option.none();
        } catch (IOException ex) {
            return Option.some(new FailedModificationError("files"));
        }
    }
}
