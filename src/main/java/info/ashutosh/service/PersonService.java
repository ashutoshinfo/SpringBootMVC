package info.ashutosh.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import info.ashutosh.dto.AllPersonDto;
import info.ashutosh.dto.PersonRegistrationDto;
import info.ashutosh.dto.UserCred;
import info.ashutosh.entity.Department;
import info.ashutosh.entity.Person;
import info.ashutosh.repository.DepartmentRepository;
import info.ashutosh.repository.PersonRepository;
import info.ashutosh.utility.CommonMethod;
import jakarta.servlet.http.HttpSession;
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
            modelAndView.addObject("msg", "User with this email already exists");
            modelAndView.addObject("departments", departmentService.getAllDepartments());
            return false;
        }

        if (department.isPresent()) {
            personRepository.save(new Person(dto.getName(), dto.getEmail(), dto.getPassword(), department.get()));
            return true;
        } else {
            modelAndView.addObject("msg", "Department not found");
            modelAndView.addObject("departments", departmentService.getAllDepartments());
            return false;
        }
    }


    public List<AllPersonDto> getAllPersons() {
        return personRepository.findAll().stream().map(person -> {
            Department department = person.getDepartment();
            return new AllPersonDto(
                person.getId(),
                person.getName(),
                department != null ? department.getDeptname() : null,
                person.getEmail(),
                person.getPassword()
            );
        }).collect(Collectors.toList());
    }

    public AllPersonDto getPersonDtoById(Long personId) {
        return personRepository.findById(personId).map(person ->
            new AllPersonDto(
                person.getId(),
                person.getName(),
                person.getDepartment().getDeptid(),
                person.getEmail(),
                person.getPassword()
            )).orElse(null);
    }

    public boolean updatePerson(AllPersonDto editPersonDto, Model model) {
        // Check if an email already exists for another user
        Optional<Person> existingPerson = personRepository.findByEmail(editPersonDto.getEmail());
        if (existingPerson.isPresent() && !existingPerson.get().getId().equals(editPersonDto.getPersonId())) {
            
            model.addAttribute("editPersonDto", editPersonDto);
            model.addAttribute("errorMsg", "A user with this email already exists.");
            model.addAttribute("departments", departmentService.getAllDepartments());
            return false;
        }

        // Proceed with updating the person
        return personRepository.findById(editPersonDto.getPersonId()).map(person -> {
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

    public boolean checkCredentials(@Valid UserCred cred, HttpSession session) {
        Person person = new Person();
        person.setEmail(cred.getUsername());
        person.setPassword(cred.getPassword());

        Optional<Person> findOne = personRepository.findOne(Example.of(person));
        if (findOne.isPresent()) {
            session.setAttribute("myAttribute", "Hello, Session!");
            return true;
        } else {
            return false;

        }

    }
}
