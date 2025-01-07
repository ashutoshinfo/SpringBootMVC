package info.ashutosh.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import info.ashutosh.dto.UserCred;
import info.ashutosh.service.PersonService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user")
public class UserController {

    private final PersonService personService;

    @Autowired
    public UserController(PersonService personService) {
        this.personService = personService;
    }

    /**
     * Handles the GET request to display the login page.
     * Redirects to the home page if the user is already logged in.
     */
    @GetMapping("/login")
    public String login(Model model, HttpSession session) {
        if (session.getAttribute("myAttribute") != null) {
            return "redirect:/user/home";
        }
        model.addAttribute("newCred", new UserCred());
        return "login";
    }

    /**
     * Handles the GET request to log out the user and invalidate the session.
     * Redirects to the home page after logout.
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/user/home";
    }

    /**
     * Handles the POST request to validate user credentials.
     * Redirects to the home page if credentials are valid.
     * Redirects back to the login page with an error message if invalid.
     */
    @PostMapping("/check_cred")
    public String checkLogin(
        @ModelAttribute("userCred") @Valid UserCred cred,
        HttpSession session,
        RedirectAttributes redirectAttributes) {

        boolean isValidCredential = personService.checkCredentials(cred, session);

        if (isValidCredential) {
            return "redirect:/user/home";
        }

        redirectAttributes.addFlashAttribute("username", cred.getUsername());
        redirectAttributes.addFlashAttribute("errorMsg", "Invalid credentials. Please try again.");
        return "redirect:/user/login";
    }

    /**
     * Handles the GET request to display the home page.
     */
    @GetMapping("/home")
    public String home() {
        return "home";
    }
}
