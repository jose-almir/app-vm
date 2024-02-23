package eventy.domain.errors;


public class ResourceNotFoundError extends DomainError {
    public ResourceNotFoundError(String resourceName) {
        super(String.format("%s not found.", resourceName));
    }
}
