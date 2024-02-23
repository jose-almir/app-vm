package eventy.services.interfaces;

import eventy.domain.errors.DomainError;
import io.vavr.control.Either;
import io.vavr.control.Option;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    Either<DomainError, String> upload(MultipartFile file, String folder);
    Option<DomainError> delete(String resource);
}
