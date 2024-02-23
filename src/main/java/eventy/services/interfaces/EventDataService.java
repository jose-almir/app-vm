package eventy.services.interfaces;

import eventy.domain.Event;
import eventy.domain.enums.EventStatus;
import eventy.domain.errors.DomainError;
import io.vavr.control.Option;

public interface EventDataService extends DataService<Long, Event> {
    Option<DomainError> changeStatus(Long id, EventStatus status);
}
