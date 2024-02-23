package eventy.services.interfaces;

import eventy.domain.User;
import eventy.domain.errors.DomainError;
import io.vavr.control.Either;
import io.vavr.control.Option;

public interface UserDataService extends DataService<Long, User> {

    Option<DomainError> block(Long id);

    Option<DomainError> unblock(Long id);

    Either<DomainError, User> getByEmail(String email);

    Option<DomainError> changePassword(User user, String oldPassword, String newPassword);

}
