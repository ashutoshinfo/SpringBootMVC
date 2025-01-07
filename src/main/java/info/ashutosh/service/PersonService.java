package info.ashutosh.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import info.ashutosh.utility.SessionUtility;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import info.ashutosh.dto.PersonDto;
import info.ashutosh.dto.PersonRegistrationDto;
import info.ashutosh.dto.UserCred;
import info.ashutosh.entity.Department;
import info.ashutosh.entity.Person;
import info.ashutosh.repository.DepartmentRepository;
import info.ashutosh.repository.PersonRepository;
import info.ashutosh.utility.CommonMethod;
import jakarta.validation.Valid;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final DepartmentRepository departmentRepository;
    private final DepartmentService departmentService;

    @Autowired
    public PersonService(PersonRepository personRepository, DepartmentRepository departmentRepository, DepartmentService departmentService) {
        this.personRepository = personRepository;
        this.departmentRepository = departmentRepository;
        this.departmentService = departmentService;
    }

    public boolean registerUser(PersonRegistrationDto dto, ModelAndView modelAndView) {
        Long departmentId = CommonMethod.stringToLong(dto.getDepartId());
        Optional<Department> department = departmentRepository.findById(departmentId);

        if (personRepository.findByEmail(dto.getEmail()).isPresent()) {
            modelAndView.addObject("dtoObject", dto);
            modelAndView.addObject("msg", "User with this email already exists");
            modelAndView.addObject("departments", departmentService.getAllDepartments());
            return false;
        }

        if (department.isPresent()) {
            personRepository.save(new Person(dto.getPersonName(), dto.getEmail(), dto.getPassword(), department.get()));
            return true;
        } else {
            modelAndView.addObject("msg", "Department not found");
            modelAndView.addObject("departments", departmentService.getAllDepartments());
            return false;
        }
    }


    public List<PersonDto> getAllPersons() {
        return personRepository.findAll().stream().map(person -> {
            Department department = person.getDepartment();
            return new PersonDto(
                    person.getId(),
                    person.getName(),
                    department != null ? department.getDeptName() : null,
                    person.getEmail(),
                    person.getPassword()
            );
        }).collect(Collectors.toList());
    }

    public PersonDto getPersonDtoById(Long personId) {
        return personRepository.findById(personId).map(person ->
                new PersonDto(
                        person.getId(),
                        person.getName(),
                        person.getDepartment().getDeptId(),
                        person.getEmail(),
                        person.getPassword()
                )).orElse(null);
    }

    public boolean updatePerson(PersonDto editPersonDto, Model model) {
        // Check if an email already exists for another user
        Optional<Person> existingPerson = personRepository.findByEmail(editPersonDto.getEmail());
        if (existingPerson.isPresent() && !existingPerson.get().getId().equals(editPersonDto.getId())) {

            model.addAttribute("isEditMode", true); // In edit mode
            model.addAttribute("editPersonDto", editPersonDto);
            model.addAttribute("msg", "A user with this email already exists.");
            model.addAttribute("departments", departmentService.getAllDepartments());
            return false;
        }

        // Proceed with updating the person
        return personRepository.findById(editPersonDto.getId()).map(person -> {
            departmentRepository.findById(editPersonDto.getDepartId()).ifPresent(department -> {
                person.setName(editPersonDto.getPersonName());
                person.setDepartment(department);
                person.setEmail(editPersonDto.getEmail());

                // Keep the existing password if not provided
                if (editPersonDto.getPassword() != null && !editPersonDto.getPassword().isBlank()) {
                    person.setPassword(editPersonDto.getPassword());
                }

                personRepository.save(person);
            });
            return true;
        }).orElse(false);
    }


    public void deletePerson(Long personId) {
        personRepository.deleteById(personId);
    }

    public boolean checkCredentials(@Valid UserCred cred, HttpServletRequest request, HttpServletResponse response) {

        Person person = new Person();
        person.setEmail(cred.getUsername());
        person.setPassword(cred.getPassword());

        // Find the person in the repository using the provided credentials
        Optional<Person> optional = personRepository.findOne(Example.of(person));

        // If a person is found, check session validity
        if (optional.isPresent()) {
            Person existing = optional.get();
            SessionUtility.createSession(request, response, existing.getId().toString(), existing.getEmail());
            return true;
        }

        // Return false if no matching person is found
        return false;
    }

}
