package eventy.domain.repositories;

import eventy.domain.User;
import eventy.domain.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByRole(Role role);
    Optional<User> findByEmail(String email);
}
