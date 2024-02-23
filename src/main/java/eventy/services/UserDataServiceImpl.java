package eventy.services;

import eventy.domain.User;
import eventy.domain.enums.Role;
import eventy.domain.errors.DomainError;
import eventy.domain.errors.FailedModificationError;
import eventy.domain.errors.ResourceNotFoundError;
import eventy.domain.repositories.UserRepository;
import eventy.services.interfaces.UserDataService;
import io.vavr.control.Either;
import io.vavr.control.Option;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserDataServiceImpl implements UserDataService {
    private final String RESOURCE_NAME = "user";

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserDataServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> list() {
        return userRepository.findByRole(Role.COMMON);
    }

    @Override
    public Either<DomainError, User> get(Long id) {
        Optional<User> userOpt = userRepository.findById(id);

        return userOpt.<Either<DomainError, User>>map(Either::right).orElseGet(() -> Either.left(new ResourceNotFoundError(RESOURCE_NAME)));
    }

    @Override
    public Option<DomainError> add(User user) {
        try {
            user.setId(null);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return Option.none();
        } catch (Exception ex) {
            return Option.some(new FailedModificationError(RESOURCE_NAME));
        }
    }

    @Override
    public Option<DomainError> update(Long id, User user) {
        return get(id).fold(Option::some, (curUser) -> {
            try {
                user.setId(id);
                userRepository.save(user);
            } catch (Exception ex) {
                return Option.some(new FailedModificationError(RESOURCE_NAME));
            }
            return Option.none();
        });
    }

    @Override
    public Option<DomainError> block(Long id) {
        return get(id).fold(Option::some, (curUser) -> {
            curUser.setBlocked(true);
            userRepository.save(curUser);
            return Option.none();
        });
    }

    @Override
    public Option<DomainError> unblock(Long id) {
        return get(id).fold(Option::some, (curUser) -> {
            curUser.setBlocked(false);
            userRepository.save(curUser);
            return Option.none();
        });
    }

    @Override
    public Option<DomainError> delete(Long id) {
        return get(id).fold(Option::of, (a) -> {
            try {
                userRepository.deleteById(id);
            } catch (Exception ex) {
                return Option.some(new FailedModificationError(RESOURCE_NAME));
            }

            return Option.none();
        });
    }

    @Override
    public Either<DomainError, User> getByEmail(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        return userOpt.<Either<DomainError, User>>map(Either::right).orElseGet(() -> Either.left(new ResourceNotFoundError(RESOURCE_NAME)));
    }

    @Override
    public Option<DomainError> changePassword(User user, String oldPassword, String newPassword) {
        try {
            if (passwordEncoder.matches(oldPassword, user.getPassword())) {
                user.setPassword(passwordEncoder.encode(newPassword));

                userRepository.save(user);
                return Option.none();
            } else {
                return Option.some(new FailedModificationError(RESOURCE_NAME));
            }
        } catch (Exception ex) {
            return Option.some(new FailedModificationError(RESOURCE_NAME));
        }
    }
}
