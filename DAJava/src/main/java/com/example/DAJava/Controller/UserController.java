package com.example.DAJava.Controller;

import com.example.DAJava.Model.User;
import com.example.DAJava.Service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/login")
    public String login(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "users/login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model, OAuth2AuthenticationToken authenticationToken) {
        if (authenticationToken != null) {
            OAuth2User oAuth2User = authenticationToken.getPrincipal();
            User user = new User();
            user.setUsername(oAuth2User.getAttribute("name"));
            user.setEmail(oAuth2User.getAttribute("email"));
            model.addAttribute("user", user);
        } else {
            model.addAttribute("user", new User());
        }
        return "users/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user,
                           @NotNull BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            return "users/register";
        }
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            model.addAttribute("errors", new String[]{"Passwords do not match"});
            return "users/register";
        }
        if (userService.existsByUsername(user.getUsername())) {
            model.addAttribute("errors", new String[]{"Username already taken"});
            return "users/register";
        }

        userService.save(user);
        userService.setDefaultRole(user.getUsername());
        return "redirect:/login";
    }

    @GetMapping("/oauth2/authorization/facebook")
    public String loginWithFacebook(Model model, OAuth2AuthenticationToken authenticationToken) {
        if (authenticationToken != null) {
            OAuth2User oAuth2User = authenticationToken.getPrincipal();
            User user = new User();
            // Example: Set Facebook attributes to User object
            user.setUsername(oAuth2User.getAttribute("name"));
            user.setEmail(oAuth2User.getAttribute("email"));
            // Add more attributes as needed

            model.addAttribute("user", user);
        } else {
            model.addAttribute("user", new User());
        }
        return "users/register"; // Redirect to register.html with Facebook user data
    }
}
