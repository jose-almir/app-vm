package eventy.utils;

import java.time.LocalDate;
import java.util.List;

import eventy.domain.enums.EventStatus;
import eventy.domain.enums.RegistrationStatus;
import eventy.domain.enums.Role;
import eventy.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import eventy.domain.Event;
import eventy.domain.Registration;
import eventy.domain.User;
import eventy.domain.repositories.EventRepository;
import eventy.domain.repositories.RegistrationRepository;
import eventy.domain.repositories.UserRepository;
import jakarta.annotation.PostConstruct;

@Service
@Profile({ "dev", "test", "prod" })
public class PopulateService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    private void init() {
        User root = new User("root", "root@eventy.com", LocalDate.of(2024, 1, 1), passwordEncoder.encode("root"));
        root.setRole(Role.ROOT);
        User user1 = new User("Alice Johnson", "alice@example.com", LocalDate.of(1990, 1, 15), passwordEncoder.encode("12345678"));
        User user2 = new User("Bob Smith", "bob@example.com", LocalDate.of(1985, 5, 22), passwordEncoder.encode("12345678"));
        User user3 = new User("Charlie Brown", "charlie@example.com", LocalDate.of(1995, 8, 7), passwordEncoder.encode("12345678"));
        User user4 = new User("David Miller", "david@example.com", LocalDate.of(1988, 12, 3), passwordEncoder.encode("12345678"));
        user4.setBlocked(true);
        User user5 = new User("Eva Davis", "eva@example.com", LocalDate.of(1992, 3, 30), passwordEncoder.encode("12345678"));
        User user6 = new User("Frank White", "frank@example.com", LocalDate.of(1987, 6, 18), passwordEncoder.encode("12345678"));
        User user7 = new User("Grace Adams", "grace@example.com", LocalDate.of(1993, 10, 12), passwordEncoder.encode("12345678"));
        User user8 = new User("Henry Turner", "henry@example.com", LocalDate.of(1989, 2, 25), passwordEncoder.encode("12345678"));
        User user9 = new User("Ivy Lee", "ivy@example.com", LocalDate.of(1994, 7, 8), passwordEncoder.encode("12345678"));
        User user10 = new User("Jack Robinson", "jack@example.com", LocalDate.of(1991, 11, 4), passwordEncoder.encode("12345678"));

        userRepository.saveAll(List.of(root, user1, user2, user3, user4, user5, user6, user7, user8, user9, user10));

        Event event1 = new Event("Java Workshop", "Hands-on Java programming workshop", LocalDate.of(2024, 2, 10),
                "Tech Hub, City Center");
        event1.setStatus(EventStatus.PENDING);
        Event event2 = new Event("Web Development Conference", "Latest trends in web development",
                LocalDate.of(2024, 3, 5), "Convention Center, Downtown");
        Event event3 = new Event("Data Science Summit", "Exploring the world of data science",
                LocalDate.of(2024, 4, 15), "Analytics Tower, Business District");
        Event event4 = new Event("Agile Expo", "Agile methodologies and best practices", LocalDate.of(2024, 5, 20),
                "Innovation Hall, Tech Park");
        Event event5 = new Event("UX/UI Design Forum", "Interactive sessions on user experience and interface design",
                LocalDate.of(2024, 6, 8), "Design Studio, Creative Zone");

        eventRepository.saveAll(List.of(event1, event2, event3, event4, event5));

        Registration registration1 = new Registration(user1, event1, LocalDate.now().minusDays(5));
        registration1.setStatus(RegistrationStatus.CONFIRMED);
        Registration registration2 = new Registration(user2, event1, LocalDate.now().minusDays(8));
        Registration registration3 = new Registration(user3, event2, LocalDate.now().minusDays(15));
        Registration registration4 = new Registration(user4, event2, LocalDate.now().minusDays(20));
        Registration registration5 = new Registration(user5, event3, LocalDate.now().minusDays(25));
        Registration registration6 = new Registration(user6, event3, LocalDate.now().minusDays(30));
        Registration registration7 = new Registration(user7, event4, LocalDate.now().minusDays(12));
        Registration registration8 = new Registration(user8, event4, LocalDate.now().minusDays(18));
        Registration registration9 = new Registration(user9, event5, LocalDate.now().minusDays(22));
        Registration registration10 = new Registration(user10, event5, LocalDate.now().minusDays(28));
        Registration registration11 = new Registration(user10, event2, LocalDate.now().minusDays(28));
        Registration registration12 = new Registration(user1, event3, LocalDate.now().minusDays(28));
        registration12.setStatus(RegistrationStatus.PENDING);


        registrationRepository.saveAll(List.of(
                registration1, registration2, registration3, registration4, registration5,
                registration6, registration7, registration8, registration9, registration10, registration11, registration12));
    }
}
