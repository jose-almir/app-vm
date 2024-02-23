package eventy.domain.errors;

import lombok.Getter;
import lombok.ToString;

import java.time.Instant;

@ToString
@Getter
public class DomainError extends RuntimeException {
    private final String message;
    private final Instant timestamp;

    public DomainError(String message) {
        super(message);
        this.message = message;
        this.timestamp = Instant.now();
    }
}
