package eventy.domain.repositories;

import eventy.domain.Event;
import eventy.domain.Registration;
import eventy.domain.RegistrationKey;
import eventy.domain.User;
import eventy.domain.enums.RegistrationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface RegistrationRepository extends JpaRepository<Registration, RegistrationKey> {
    List<Registration> findByUser(User user);
    List<Registration> findByEvent(Event event);

    List<Registration> findByUserAndStatusIn(User user, Collection<RegistrationStatus> statuses);
}
