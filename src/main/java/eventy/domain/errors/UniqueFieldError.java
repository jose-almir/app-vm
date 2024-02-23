package eventy.domain.errors;

public class UniqueFieldError extends DomainError {
    public UniqueFieldError(String resourceName, String field, String value) {
        super(String.format("Already exists a %s with '%s' equals %s", resourceName, field, value));
    }
}
