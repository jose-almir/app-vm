package eventy.controllers;

import eventy.controllers.dtos.EditProfileForm;
import eventy.domain.Registration;
import eventy.domain.User;
import eventy.domain.enums.RegistrationStatus;
import eventy.security.CustomUserDetails;
import eventy.services.interfaces.RegistrationDataService;
import eventy.services.interfaces.UploadService;
import eventy.services.interfaces.UserDataService;
import eventy.utils.UploadFileUtils;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final RegistrationDataService registrationDataService;

    private final UserDataService userDataService;

    private final UploadService uploadService;

    public ProfileController(RegistrationDataService registrationDataService, UserDataService userDataService, UploadService uploadService) {
        this.registrationDataService = registrationDataService;
        this.userDataService = userDataService;
        this.uploadService = uploadService;
    }

    @GetMapping
    @PreAuthorize("hasRole('COMMON')")
    public ModelAndView profilePage(@AuthenticationPrincipal CustomUserDetails logged) {
        User user = logged.getUser();
        List<Registration> registrations = registrationDataService.listUserRegistrations(user);

        return new ModelAndView("profile")
                .addObject("user", user)
                .addObject("confirmed", registrations.stream().filter(reg -> reg.getStatus() == RegistrationStatus.CONFIRMED).toList())
                .addObject("requests", registrations.stream().filter(reg -> reg.getStatus() == RegistrationStatus.REJECTED || reg.getStatus() == RegistrationStatus.PENDING).toList());
    }

    @GetMapping("/edit")
    @PreAuthorize("hasRole('COMMON')")
    public String profileEditPage(@AuthenticationPrincipal CustomUserDetails logged, Model template) {
        if (!template.containsAttribute("form")) {
            template.addAttribute("form", EditProfileForm.fromEntity(logged.getUser()));
        }

        return "profile-edit";
    }

    @PostMapping("/edit")
    @PreAuthorize("hasRole('COMMON')")
    public String profileEditAction(@Valid @ModelAttribute("form") EditProfileForm form, BindingResult bindingResult, RedirectAttributes attrs, @AuthenticationPrincipal CustomUserDetails logged) {
        if (bindingResult.hasErrors()) {
            attrs.addFlashAttribute("org.springframework.validation.BindingResult.form", bindingResult);
            attrs.addFlashAttribute("form", form);

            return "redirect:/profile/edit";
        }

        User user = logged.getUser();

        user.setFullname(form.getFullname());
        user.setBirthdate(form.getBirthdate());

        return userDataService.update(user.getId(), user).fold(() -> "redirect:/profile", (ex) -> {
            attrs.addFlashAttribute("error", ex.getMessage());
            return "redirect:/profile/edit";
        });
    }

    @PostMapping("/picture-upload")
    @PreAuthorize("hasRole('COMMON')")
    public String profilePictureUpload(@RequestParam("picture") MultipartFile file, RedirectAttributes attrs, @AuthenticationPrincipal CustomUserDetails logged) {
        String redirectUrl = "redirect:/profile";
        String errorMessage;

        if (file.isEmpty()) {
            errorMessage = "Select a file";
        } else if (!UploadFileUtils.hasExtension(Objects.requireNonNull(file.getOriginalFilename()), "jpg", "png", "jpeg")) {
            errorMessage = "Invalid Format";
        } else {
            errorMessage = uploadService.upload(file, "users").fold(Exception::getMessage, (url) -> {
                User user = logged.getUser();
                if (user.getPictureUrl() != null) uploadService.delete(user.getPictureUrl());
                user.setPictureUrl(url);
                return userDataService.update(user.getId(), user).fold(() -> null, Exception::getMessage);
            });
        }

        if (errorMessage != null) {
            attrs.addFlashAttribute("error", errorMessage);
        }

        return redirectUrl;
    }
}
