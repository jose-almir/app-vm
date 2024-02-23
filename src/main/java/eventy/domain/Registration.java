package eventy.domain;

import eventy.domain.enums.RegistrationStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "registrations")
@Data
@NoArgsConstructor
public class Registration {
    @EmbeddedId
    private RegistrationKey id;

    @NonNull
    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @NonNull
    @ManyToOne
    @MapsId("eventId")
    @JoinColumn(name = "event_id")
    private Event event;

    @NonNull
    @Column(nullable = false)
    private LocalDate date;

    @NonNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RegistrationStatus status = RegistrationStatus.PENDING;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public Registration(User user, Event event, LocalDate date) {
        this.id = new RegistrationKey(user.getId(), event.getId());
        this.user = user;
        this.event = event;
        this.date = date;
    }
}
