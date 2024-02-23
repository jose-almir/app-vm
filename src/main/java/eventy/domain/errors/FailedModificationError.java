package eventy.domain.errors;

public class FailedModificationError extends DomainError {
    public FailedModificationError(String resourceName) {
        super(String.format("Failed to modify a %s for some reason", resourceName));
    }
}
