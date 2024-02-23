package eventy.domain;

import eventy.domain.enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(nullable = false)
    private String fullname;

    @NonNull
    @Column(nullable = false, unique = true)
    private String email;

    @NonNull
    @Column(nullable = false)
    private LocalDate birthdate;

    @NonNull
    private String password;

    private String pictureUrl;

    @Enumerated(EnumType.STRING)
    private Role role = Role.COMMON;

    private Boolean blocked = false;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
