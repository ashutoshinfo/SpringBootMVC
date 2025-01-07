package info.ashutosh.controller;

import info.ashutosh.utility.SessionUtility;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import info.ashutosh.dto.UserCred;
import info.ashutosh.service.PersonService;
import jakarta.validation.Valid;

@Controller
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
    public String login(Model model, HttpServletRequest request) {
        if (SessionUtility.isSessionValid(request)) {
            return "redirect:/home";
        }
        model.addAttribute("newCred", new UserCred());
        return "login";
    }

    /**
     * Handles the GET request to log out the user and invalidate the session.
     * Redirects to the home page after logout.
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        SessionUtility.destroySession(request, response);
        return "redirect:/home";
    }

    /**
     * Handles the POST request to validate user credentials.
     * Redirects to the home page if credentials are valid.
     * Redirects back to the login page with an error message if invalid.
     */
    @PostMapping("/check_cred")
    public String checkLogin(
            @ModelAttribute("userCred") @Valid UserCred cred,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response) {

        if (personService.checkCredentials(cred, request, response)) {
            SessionUtility.createSession(request, response, cred.getUsername(), cred.getUsername());
            return "redirect:/home";
        }

        model.addAttribute("newCred", new UserCred());
        model.addAttribute("username", cred.getUsername());
        model.addAttribute("errorMsg", "Invalid credentials. Please try again.");
        return "login";
    }

    /**
     * Handles the GET request to display the home page.
     */
    @GetMapping("/home")
    public String home(HttpServletRequest request, Model model) {
//        if (!SessionUtility.isSessionValid(request)) {
//            return "redirect:/user/login";
//        }
        return "home";
    }
}
