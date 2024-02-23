package eventy.domain.errors;

public class AddUserToEventError extends DomainError{
    public AddUserToEventError() {
        super("Cannot add user to a event with status: CANCELLED or FINISHED");
    }
}
