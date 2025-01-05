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

	@Autowired
	PersonService personService;

	@GetMapping("/login")
	public String login(Model model,HttpSession session) {
		boolean check_cred = session.getAttribute("myAttribute")!=null;
		if (check_cred) {
			return "redirect:/user/home"; // Redirect to the updated list of all persons
		} else {
			model.addAttribute("new_cred", new UserCred());
			return "login"; // Redirect to the updated list of all persons
		}
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/user/home"; // Redirect to the list of all persons
	}

	@PostMapping("/check_cred")
	public String checkLogin(@ModelAttribute("userCred") @Valid UserCred cred, HttpSession session, RedirectAttributes redirectAttributes) {

		boolean check_cred = personService.check_cred(cred, session);
		if (check_cred) {
			return "redirect:/user/home"; // Redirect to the home page
		} else {
			redirectAttributes.addFlashAttribute("username", cred.getUsername());
			redirectAttributes.addFlashAttribute("msg", "Invalid credentials. Please try again.");
			return "redirect:/user/login"; // Redirect back to the login page
		}
	}


	@GetMapping("/home")
	public String home() {
		return "home";
	}


}