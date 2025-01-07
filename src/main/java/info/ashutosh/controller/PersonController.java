package info.ashutosh.controller;

import info.ashutosh.utility.SessionUtility;
import jakarta.servlet.http.HttpServletRequest;
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
@RequestMapping
public class PersonController {

    private final DepartmentService departmentService;
    private final PersonService personService;

    @Autowired
    public PersonController(DepartmentService departmentService, PersonService personService) {
        this.departmentService = departmentService;
        this.personService = personService;
    }

    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("isEditMode", false); // Not in edit mode
        model.addAttribute("dtoObject", new PersonRegistrationDto());
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "registration_edit";
    }

    @PostMapping("/register")
    public ModelAndView registerUser(@ModelAttribute("dtoObject") @Valid PersonRegistrationDto dtoObject) {
        ModelAndView modelAndView = new ModelAndView();
        if (personService.registerUser(dtoObject, modelAndView)) {
            modelAndView.setViewName("success");
        } else {
            modelAndView.setViewName("registration_edit");
        }
        return modelAndView;
    }

    @GetMapping("/all")
    public String listAllPersons(Model model, HttpSession session) {
        if (session.getAttribute("myAttribute") == null) {
            return "redirect:/login";
        }
        model.addAttribute("allPersons", personService.getAllPersons());
        return "allPersons";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, HttpServletRequest request) {

        if (SessionUtility.isSessionValid(request)) {
            return "redirect:/login";
        }
        AllPersonDto dtoObject = personService.getPersonDtoById(id);
        if (dtoObject == null) {
            return "error";
        }
        model.addAttribute("isEditMode", true); // In edit mode
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("dtoObject", dtoObject);
        return "registration_edit";
    }

    @PostMapping("/edit/{id}")
    public String updatePerson(@PathVariable Long id, @ModelAttribute("dtoObject") AllPersonDto dtoObject, Model model, HttpSession session) {
        if (session.getAttribute("myAttribute") == null) {
            return "redirect:/login";
        }

        boolean isUpdated = personService.updatePerson(dtoObject, model);
        if (!isUpdated) {
            // If email conflict or other issue occurs, redirect back to the edit page
            return "registration_edit"; // Replace with the name of the edit page template
        }

        return "redirect:/all";
    }

    @PostMapping("/delete/{id}")
    public String deletePerson(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("myAttribute") == null) {
            return "redirect:/login";
        }
        personService.deletePerson(id);
        return "redirect:/all";
    }
}
