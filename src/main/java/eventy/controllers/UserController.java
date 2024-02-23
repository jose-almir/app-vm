package eventy.controllers;

import eventy.services.interfaces.RegistrationDataService;
import eventy.services.interfaces.UserDataService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserDataService userDataService;

    private final RegistrationDataService registrationDataService;

    public UserController(UserDataService userDataService, RegistrationDataService registrationDataService) {
        this.userDataService = userDataService;
        this.registrationDataService = registrationDataService;
    }

    @GetMapping
    public ModelAndView users() {
        return new ModelAndView("users/list").addObject("users", userDataService.list());
    }

    @GetMapping("/{id}")
    public ModelAndView userDetail(@PathVariable Long id, RedirectAttributes attrs) {
        return userDataService.get(id).fold(
                (ex) -> {
                    attrs.addFlashAttribute("error", ex.getMessage());
                    return new ModelAndView("redirect:/users");
                },
                (user) -> new ModelAndView("users/detail")
                        .addObject("user", user)
                        .addObject("registrations", registrationDataService.listUserRegistrations(user))
        );
    }

    @PostMapping("/{id}/block")
    public String userBlockAction(@PathVariable Long id, RedirectAttributes attrs, @RequestParam(defaultValue = "users") String url) {
        return userDataService.block(id).fold(() -> "redirect:/" + url, (ex) -> {
            attrs.addFlashAttribute("error", ex.getMessage());
            return "redirect:/" + url;
        });
    }

    @PostMapping("/{id}/unblock")
    public String userUnblockAction(@PathVariable Long id, RedirectAttributes attrs, @RequestParam(defaultValue = "users") String url) {
        return userDataService.unblock(id).fold(() -> "redirect:/" + url, (ex) -> {
            attrs.addFlashAttribute("error", ex.getMessage());
            return "redirect:/" + url;
        });
    }
}
