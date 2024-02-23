package eventy.controllers.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordForm {
    @NotBlank(message = "Password is required")
    private String oldPassword;


    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Min. 6 characters")
    private String newPassword;
}
