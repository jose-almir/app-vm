package eventy.services;

import eventy.domain.Event;
import eventy.domain.enums.EventStatus;
import eventy.domain.errors.DomainError;
import eventy.domain.errors.FailedModificationError;
import eventy.domain.errors.ResourceNotFoundError;
import eventy.domain.repositories.EventRepository;
import eventy.services.interfaces.EventDataService;
import eventy.services.interfaces.UploadService;
import io.vavr.control.Either;
import io.vavr.control.Option;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventDataServiceImpl implements EventDataService {
    private final String RESOURCE_NAME = "event";
    private final EventRepository eventRepository;

    private final UploadService uploadService;

    public EventDataServiceImpl(EventRepository eventRepository, UploadService uploadService) {
        this.eventRepository = eventRepository;
        this.uploadService = uploadService;
    }

    @Override
    public List<Event> list() {
        return eventRepository.findAll();
    }

    @Override
    public Either<DomainError, Event> get(Long id) {
        Optional<Event> eventOpt = eventRepository.findById(id);

        return eventOpt.<Either<DomainError, Event>>map(Either::right).orElseGet(() -> Either.left(new ResourceNotFoundError(RESOURCE_NAME)));
    }

    @Override
    public Option<DomainError> add(Event event) {
        try {
            event.setId(null);
            eventRepository.save(event);
            return Option.none();
        } catch (Exception ex) {
            return Option.some(new FailedModificationError(RESOURCE_NAME));
        }
    }

    @Override
    public Option<DomainError> update(Long id, Event event) {
        return get(id).fold(Option::some, (curEvent) -> {
            try {
                event.setId(id);
                if(curEvent.getBannerUrl() != null) {
                    if(!curEvent.getBannerUrl().equals(event.getBannerUrl())) {
                        uploadService.delete(curEvent.getBannerUrl());
                    }
                }
                eventRepository.save(event);
            } catch (Exception ex) {
                return Option.some(new FailedModificationError(RESOURCE_NAME));
            }
            return Option.none();
        });
    }

    @Override
    public Option<DomainError> delete(Long id) {
        return get(id).fold(Option::of, (unused) -> {
            try {
                eventRepository.deleteById(id);
            } catch (Exception ex) {
                return Option.some(new FailedModificationError(RESOURCE_NAME));
            }

            return Option.none();
        });
    }

    @Override
    public Option<DomainError> changeStatus(Long id, EventStatus status) {
        return get(id).fold(Option::of, (event) -> {
            event.setStatus(status);

            try {
                eventRepository.save(event);
            } catch (Exception ex) {
                return Option.some(new FailedModificationError(RESOURCE_NAME));
            }

            return Option.none();
        });
    }
}
