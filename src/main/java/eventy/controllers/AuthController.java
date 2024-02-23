package eventy.controllers;

import eventy.controllers.dtos.ChangePasswordForm;
import eventy.controllers.dtos.EditProfileForm;
import eventy.controllers.dtos.SignupUserForm;
import eventy.security.CustomUserDetails;
import eventy.services.interfaces.UserDataService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class AuthController {

    private final UserDataService userDataService;

    public AuthController(UserDataService userDataService) {
        this.userDataService = userDataService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/signup")
    public String signupPage(Model template) {
        if (!template.containsAttribute("form")) {
            template.addAttribute("form", new SignupUserForm());
        }
        return "signup";
    }

    @PostMapping("/signup")
    public String registerUser(@Valid @ModelAttribute("form") SignupUserForm form, BindingResult bindingResult, RedirectAttributes attrs) {
        if (bindingResult.hasErrors()) {
            attrs.addFlashAttribute("org.springframework.validation.BindingResult.form", bindingResult);
            attrs.addFlashAttribute("form", form);

            return "redirect:/signup";
        }


        return userDataService.add(form.toEntity()).fold(() -> "redirect:/login", (ex) -> {
            attrs.addFlashAttribute("error", ex.getMessage());
            attrs.addFlashAttribute("form", form);

            return "redirect:/signup";
        });
    }

    @GetMapping("/change-password")
    public String changePasswordPage(Model template) {
        if (!template.containsAttribute("form")) {
            template.addAttribute("form", new ChangePasswordForm());
        }
        return "change-password";
    }

    @PostMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public String changePasswordAction(@Valid @ModelAttribute ChangePasswordForm form, BindingResult bindingResult, RedirectAttributes attrs, @AuthenticationPrincipal CustomUserDetails logged) {
        if (bindingResult.hasErrors()) {
            attrs.addFlashAttribute("org.springframework.validation.BindingResult.form", bindingResult);
            attrs.addFlashAttribute("form", form);

            return "redirect:/change-password";
        }
        System.out.println(logged.getUser().getPassword());

        return userDataService.changePassword(logged.getUser(), form.getOldPassword(), form.getNewPassword()).fold(() -> "redirect:/profile", (ex) -> {
            attrs.addFlashAttribute("error", ex.getMessage());
            return "redirect:/change-password";
        });
    }
}
