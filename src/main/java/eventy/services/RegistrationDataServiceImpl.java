package eventy.services;

import eventy.domain.Event;
import eventy.domain.Registration;
import eventy.domain.RegistrationKey;
import eventy.domain.User;
import eventy.domain.enums.EventStatus;
import eventy.domain.enums.RegistrationStatus;
import eventy.domain.errors.AddUserToEventError;
import eventy.domain.errors.DomainError;
import eventy.domain.errors.FailedModificationError;
import eventy.domain.errors.ResourceNotFoundError;
import eventy.domain.repositories.RegistrationRepository;
import eventy.services.interfaces.EventDataService;
import eventy.services.interfaces.RegistrationDataService;
import eventy.services.interfaces.UserDataService;
import io.vavr.control.Either;
import io.vavr.control.Option;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class RegistrationDataServiceImpl implements RegistrationDataService {
    private final String RESOURCE_NAME = "registration";
    private final RegistrationRepository registrationRepository;
    private final UserDataService userDataService;
    private final EventDataService eventDataService;

    public RegistrationDataServiceImpl(RegistrationRepository registrationRepository, UserDataService userDataService, EventDataService eventDataService) {
        this.registrationRepository = registrationRepository;
        this.userDataService = userDataService;
        this.eventDataService = eventDataService;
    }

    @Override
    public List<Registration> list() {
        return registrationRepository.findAll();
    }

    @Override
    public Either<DomainError, Registration> get(RegistrationKey id) {
        Optional<Registration> registrationOpt = registrationRepository.findById(id);

        return registrationOpt.<Either<DomainError, Registration>>map(Either::right).orElseGet(() -> Either.left(new ResourceNotFoundError("registration")));

    }

    @Override
    public Option<DomainError> add(Registration registration) {
        try {
            registrationRepository.save(registration);
            return Option.none();
        } catch (Exception ex) {
            return Option.some(new FailedModificationError(RESOURCE_NAME));
        }
    }

    @Override
    public Option<DomainError> update(RegistrationKey id, Registration registration) {
        return get(id).fold(Option::of, (curRegistration) -> {
            try {
                registration.setId(id);
                registrationRepository.save(registration);
            } catch (Exception ex) {
                return Option.some(new FailedModificationError(RESOURCE_NAME));
            }

            return Option.none();
        });
    }

    @Override
    public Option<DomainError> delete(RegistrationKey id) {
        return get(id).fold(Option::of, (curRegistration) -> {
            try {
                registrationRepository.deleteById(id);
            } catch (Exception ex) {
                return Option.some(new FailedModificationError(RESOURCE_NAME));
            }

            return Option.none();
        });
    }

    @Override
    public Option<DomainError> addToEvent(Long userId, Long eventId) {
        return userDataService.get(userId).fold(Option::of, (user) -> eventDataService.get(eventId).fold(Option::of, (event) -> {
            if (event.getStatus() == EventStatus.PENDING || event.getStatus() == EventStatus.IN_PROGRESS) {
                Registration registration = new Registration(user, event, LocalDate.now());
                return add(registration);
            }

            return Option.some(new AddUserToEventError());
        }));
    }

    @Override
    public Option<DomainError> removeFromEvent(Long userId, Long eventId) {
        return get(new RegistrationKey(userId, eventId)).fold(Option::of, registration -> {
            try {
                registrationRepository.deleteById(registration.getId());
            } catch (Exception ex) {
                return Option.some(new FailedModificationError(RESOURCE_NAME));
            }

            return Option.none();
        });
    }

    @Override
    public List<Registration> listUserRegistrations(User user) {
        return registrationRepository.findByUser(user);
    }

    @Override
    public List<Registration> listEventRegistrations(Event event) {
        return registrationRepository.findByEvent(event);
    }

    @Override
    public Option<DomainError> changeStatus(Long userId, Long eventId, RegistrationStatus status) {
        return get(new RegistrationKey(userId, eventId)).fold(Option::of, registration -> {
            registration.setStatus(status);
            registrationRepository.save(registration);

            return Option.none();
        });
    }

    @Override
    public Option<DomainError> cancelRegistration(Long userId, Long eventId) {
        return get(new RegistrationKey(userId, eventId)).fold(Option::of, (registration) -> {
            registration.setStatus(RegistrationStatus.CANCELLED);
            registrationRepository.save(registration);

            return Option.none();
        });
    }
}
