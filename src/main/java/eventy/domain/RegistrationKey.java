package eventy.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class RegistrationKey implements Serializable {
    @NonNull
    @Column(name = "user_id")
    private Long userId;

    @NonNull
    @Column(name = "event_id")
    private Long eventId;
}