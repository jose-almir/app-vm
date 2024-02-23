package eventy.services.interfaces;

import eventy.domain.Event;
import eventy.domain.User;
import eventy.domain.errors.DomainError;
import io.vavr.control.Either;
import io.vavr.control.Option;

import java.util.List;

public interface DataService<ID, E> {
    List<E> list();

    Either<DomainError, E> get(ID id);

    Option<DomainError> add(E e);

    Option<DomainError> update(ID id, E e);

    Option<DomainError> delete(ID id);
}
