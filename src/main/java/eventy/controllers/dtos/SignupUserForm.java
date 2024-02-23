package eventy.controllers.dtos;

import eventy.controllers.constraints.OfLegalAge;
import eventy.domain.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SignupUserForm {
    @NotBlank(message = "Fullname is required")
    @Size(max = 255, message = "Max. 100 characters")
    private String fullname;

    @Email
    @NotBlank(message = "Email is required")
    private String email;

    @OfLegalAge(message = "Must be over 18")
    @NotNull(message = "Birthdate is required")
    private LocalDate birthdate;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Min. 6 characters")
    private String password;

    public User toEntity() {
        User user = new User();
        user.setFullname(fullname);
        user.setEmail(email);
        user.setBirthdate(birthdate);
        user.setPassword(password);

        return user;
    }
}
