package eventy.domain;


import eventy.domain.enums.EventStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(nullable = false)
    private String title;

    @NonNull
    @Column(nullable = false)
    private String description;

    @NonNull
    @Column(nullable = false)
    private LocalDate date;

    @NonNull
    @Column(nullable = false)
    private String local;

    private String bannerUrl;

    @Enumerated(EnumType.STRING)
    private EventStatus status = EventStatus.PENDING;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
