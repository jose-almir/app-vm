package eventy.services.interfaces;

import eventy.domain.Event;
import eventy.domain.Registration;
import eventy.domain.RegistrationKey;
import eventy.domain.User;
import eventy.domain.enums.RegistrationStatus;
import eventy.domain.errors.DomainError;
import io.vavr.control.Option;

import java.util.List;

public interface RegistrationDataService extends DataService<RegistrationKey, Registration> {

    List<Registration> listEventRegistrations(Event event);
    List<Registration> listUserRegistrations(User user);
    Option<DomainError> addToEvent(Long userId, Long eventId);
    Option<DomainError> removeFromEvent(Long userId, Long eventId);

    Option<DomainError> changeStatus(Long userId, Long eventId, RegistrationStatus status);

    Option<DomainError> cancelRegistration(Long userId, Long eventId);
}
