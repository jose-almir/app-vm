package eventy.controllers.dtos;

import eventy.controllers.constraints.OfLegalAge;
import eventy.domain.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class EditProfileForm {
    @NotBlank(message = "Fullname is required")
    @Size(max = 255, message = "Max. 100 characters")
    private String fullname;

    @OfLegalAge(message = "Must be over 18")
    @NotNull(message = "Birthdate is required")
    @DateTimeFormat(pattern = "YYYY-MM-dd")
    private LocalDate birthdate;

    public static EditProfileForm fromEntity(User user) {
        EditProfileForm form = new EditProfileForm();
        form.setFullname(user.getFullname());
        form.setBirthdate(user.getBirthdate());

        return form;
    }
}
