package info.ashutosh.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import info.ashutosh.dto.AllPersonDto;
import info.ashutosh.dto.PersonRegistrationDto;
import info.ashutosh.entity.Department;
import info.ashutosh.service.DepartmentService;
import info.ashutosh.service.PersonService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/register")
public class PersonController {

	@Autowired
	DepartmentService departmentService;
	@Autowired
	PersonService personService;

	@GetMapping("/registration")
	public String showNewUserForm(Model model) {

		List<Department> departments = departmentService.getAllDepartment();
		model.addAttribute("newUser", new PersonRegistrationDto());
		model.addAttribute("departments", departments);

		return "reg";
	}

	@PostMapping("new_register")
	public ModelAndView registerUserAccount(@ModelAttribute("newUser") @Valid PersonRegistrationDto dto) {

		ModelAndView andView = new ModelAndView();
		Boolean registerUser = personService.registerUser(dto, andView);
		if (registerUser == null) {
			List<Department> departments = departmentService.getAllDepartment();
			andView.addObject("newUser", new PersonRegistrationDto());
			andView.addObject("departments", departments);
			andView.setViewName("registration");
		} else {
			andView.setViewName("success");
		}

		System.out.println(dto);
		return andView;

	}

	@GetMapping("/all")
	public String showAllPersons(Model model, HttpSession session) {

		Object attribute = session.getAttribute("myAttribute");
		if (attribute == null) {
			return "redirect:/user/login"; // Redirect to the updated list of all persons
		}

		List<AllPersonDto> allPersons = personService.getAllPersonsData();
		model.addAttribute("allPersons", allPersons);
		return "allPersons";
	}

	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable Long id, Model model, HttpSession session) {
		Object attribute = session.getAttribute("myAttribute");
		if (attribute == null) {
			return "redirect:/user/login"; // Redirect to the updated list of all persons
		}
		AllPersonDto editPersonDto = personService.getEditPersonDtoById(id);
		List<Department> departments = departmentService.getAllDepartment();
		model.addAttribute("departments", departments);

		if (editPersonDto != null) {
			model.addAttribute("editPersonDto", editPersonDto);
			return "editPerson"; // Thymeleaf template for editing
		} else {
			// Handle the case when the person is not found
			return "error"; // Thymeleaf template for displaying an error
		}
	}

	// Process the form submission for editing a person
	@PostMapping("/edit/{id}")
	public String processEditForm(@PathVariable Long id, @ModelAttribute("editPersonDto") AllPersonDto editPersonDto,
			HttpSession session) {
		Object attribute = session.getAttribute("myAttribute");
		if (attribute == null) {
			return "redirect:/user/login"; // Redirect to the updated list of all persons
		}
		personService.updatePerson(editPersonDto);
		return "redirect:/register/all"; // Redirect to the list of all persons
	}

	@PostMapping("/delete/{id}")
	public String deletePerson(@PathVariable Long id, HttpSession session) {
		boolean check_cred = session.getAttribute("myAttribute") == null;
		if (check_cred) {
//			model.addAttribute("new_cred", new UserCred());
			return "redirect:/user/login"; // Redirect to the updated list of all persons
		} else {

			personService.deletePersonById(id);
			return "redirect:/register/all"; // Redirect to the updated list of all persons
		}
	}

}
