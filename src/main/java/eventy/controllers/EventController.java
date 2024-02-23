package eventy.controllers;

import eventy.controllers.dtos.EditEventForm;
import eventy.controllers.dtos.NewEventForm;
import eventy.domain.enums.EventStatus;
import eventy.domain.enums.RegistrationStatus;
import eventy.security.CustomUserDetails;
import eventy.services.interfaces.EventDataService;
import eventy.services.interfaces.RegistrationDataService;
import eventy.services.interfaces.UploadService;
import eventy.services.interfaces.UserDataService;
import eventy.utils.UploadFileUtils;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Objects;

@Controller
@RequestMapping("/events")
public class EventController {

    private final UserDataService userDataService;
    private final RegistrationDataService registrationDataService;
    private final EventDataService eventDataService;

    private final UploadService uploadService;

    public EventController(UserDataService userDataService, RegistrationDataService registrationDataService, EventDataService eventDataService, UploadService uploadService) {
        this.userDataService = userDataService;
        this.registrationDataService = registrationDataService;
        this.eventDataService = eventDataService;
        this.uploadService = uploadService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ROOT')")
    public ModelAndView events(Principal principal) {
        System.out.println(principal.getName());
        return new ModelAndView("events/list").addObject("events", eventDataService.list());
    }

    @GetMapping("/{id}")
    public ModelAndView eventDetail(@PathVariable Long id, RedirectAttributes attrs) {
        return eventDataService.get(id).fold((ex) -> {
            attrs.addFlashAttribute("error", ex.getMessage());
            return new ModelAndView("redirect:/events");
        }, (event) -> new ModelAndView("events/detail").addObject("event", event).addObject("registrations", registrationDataService.listEventRegistrations(event)));
    }

    @PostMapping("/registrations/{eventId}/{userId}/change-status")
    public String eventRegistrationChangeStatus(@PathVariable Long eventId, @PathVariable Long userId, @RequestParam Integer status, RedirectAttributes attrs) {
        return registrationDataService.changeStatus(userId, eventId, RegistrationStatus.fromValue(status)).fold(() -> "redirect:/events/" + eventId, (ex) -> {
            attrs.addFlashAttribute("error", ex.getMessage());
            return "redirect:/events/" + eventId;
        });
    }

    @PostMapping("/registrations/{eventId}/{userId}/remove")
    public String eventRemoveRegistration(@PathVariable Long eventId, @PathVariable Long userId, RedirectAttributes attrs) {
        return registrationDataService.removeFromEvent(userId, eventId).fold(() -> "redirect:/events/" + eventId, (ex) -> {
            attrs.addFlashAttribute("error", ex.getMessage());
            return "redirect:/events/" + eventId;
        });
    }

    @GetMapping("/new")
    public String newEvent(Model template) {
        if (!template.containsAttribute("form")) {
            template.addAttribute("form", new NewEventForm());
        }
        return "events/new-event";
    }

    @GetMapping("/{id}/edit")
    public String editEvent(@PathVariable Long id, RedirectAttributes attrs, Model template) {
        return eventDataService.get(id).fold((ex) -> {
            attrs.addFlashAttribute("error", ex.getMessage());
            return "redirect:/events";
        }, (event) -> {
            if (!template.containsAttribute("form")) {
                template.addAttribute("form", EditEventForm.fromEntity(event));
            }
            return "events/edit-event";
        });
    }


    @PostMapping("/create")
    public String createEvent(@Valid @ModelAttribute("form") NewEventForm form, BindingResult bindingResult, RedirectAttributes attrs, @RequestParam("picture") MultipartFile file) {
        if (bindingResult.hasErrors()) {
            attrs.addFlashAttribute("org.springframework.validation.BindingResult.form", bindingResult);
            attrs.addFlashAttribute("form", form);
            return "redirect:/events/new";
        }

        if (!UploadFileUtils.hasExtension(Objects.requireNonNull(file.getOriginalFilename()), "jpg", "png", "jpeg")) {
            System.out.println(file.getOriginalFilename());
            attrs.addFlashAttribute("error", "Invalid format");
            return "redirect:/profile";
        }

        return uploadService.upload(file, "events").fold((ex) -> {
            attrs.addFlashAttribute("error", ex.getMessage());
            attrs.addFlashAttribute("form", form);
            return "redirect:/events/new";
        }, (url) -> {
            form.setBannerUrl(url);
            return eventDataService.add(form.toEntity()).fold(() -> {
                attrs.addFlashAttribute("success", "Event created");
                return "redirect:/events";
            }, (ex) -> {
                attrs.addFlashAttribute("error", ex.getMessage());
                attrs.addFlashAttribute("form", form);
                return "redirect:/events/new";
            });
        });
    }

    @PostMapping("/update")
    public String updateEvent(@Valid @ModelAttribute("form") EditEventForm form, BindingResult bindingResult, RedirectAttributes attrs, @RequestParam("picture") MultipartFile file) {
        if (bindingResult.hasErrors()) {
            attrs.addFlashAttribute("org.springframework.validation.BindingResult.form", bindingResult);
            attrs.addFlashAttribute("form", form);
            return "redirect:/events/".concat(form.getId().toString()).concat("/edit");
        }

        if (file.isEmpty()) {
            return updateEvent(form, attrs);
        } else {
            if (!UploadFileUtils.hasExtension(Objects.requireNonNull(file.getOriginalFilename()), "jpg", "png", "jpeg")) {
                attrs.addFlashAttribute("error", "Invalid format");
                return "redirect:/events/".concat(form.getId().toString()).concat("/edit");
            }

            return uploadService.upload(file, "events").fold((ex) -> {
                attrs.addFlashAttribute("error", ex.getMessage());
                attrs.addFlashAttribute("form", form);
                return "redirect:/events/".concat(form.getId().toString()).concat("/edit");
            }, (url) -> {
                form.setBannerUrl(url);
                return updateEvent(form, attrs);
            });
        }
    }

    private String updateEvent(EditEventForm form, RedirectAttributes attrs) {
        return eventDataService.update(form.getId(), form.toEntity()).fold(() -> {
            attrs.addFlashAttribute("success", "Event updated");
            return "redirect:/events/".concat(form.getId().toString()).concat("/edit");
        }, (ex) -> {
            attrs.addFlashAttribute("error", ex.getMessage());
            attrs.addFlashAttribute("form", form);
            return "redirect:/events/".concat(form.getId().toString()).concat("/edit");
        });
    }

    @PostMapping("/{id}/change-status")
    public String eventChangeStatus(@PathVariable Long id, @RequestParam Integer status, RedirectAttributes attrs) {
        return eventDataService.changeStatus(id, EventStatus.fromValue(status)).fold(() -> "redirect:/events/" + id, (ex) -> {
            attrs.addFlashAttribute("error", ex.getMessage());
            return "redirect:/events/" + id;
        });
    }

    @PostMapping("/registrations/{eventId}/{userId}/cancel")
    @PreAuthorize("hasRole('COMMON') and #userId == authentication.principal.user.id")
    public String userEventRegistrationCancel(@PathVariable Long eventId, @PathVariable Long userId, RedirectAttributes attrs) {
        return registrationDataService.cancelRegistration(userId, eventId).fold(() -> "redirect:/profile", (ex) -> {
            attrs.addFlashAttribute("error", ex.getMessage());
            return "redirect:/profile";
        });
    }
}


// TODO: Refactor admin urls (events, users) prefix with /admin
// TODO: Show public events
// TODO: Request participation on event
// TODO: Admin area for managing user requests
// TODO: Root area for manage admin users
// TODO: AccessDeniedException handler
// TODO: Figma Design/Styles with Tailwind
// TODO: Refactor JS Scripts
// TODO: Pagination/Search/Filter/Sort
// TODO: Refactor and improve Java Code
// TODO: Unit and integration tests