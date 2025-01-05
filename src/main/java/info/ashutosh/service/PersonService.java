package info.ashutosh.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
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

	@Autowired
	PersonRepository personRepository;

	@Autowired
	DepartmentRepository departmentRepository;

	public Boolean registerUser(PersonRegistrationDto dto, ModelAndView andView) {

		Long stringToLong = CommonMethod.stringToLong(dto.getDepartId());

		Department department = new Department();

		department.setDeptid(stringToLong);

		Optional<Department> findOne = departmentRepository.findOne(Example.of(department));

		Optional<Person> userAlreadyExist = personRepository.findByEmail(dto.getEmail());

		if (userAlreadyExist.isPresent()) {
			andView.addObject("msg", "User with email Already Existed");
			return null;
		}

		if (findOne.isPresent()) {

			Person p = new Person(dto.getName(), dto.getEmail(), dto.getPassword(), findOne.get());
			personRepository.save(p);
		} else {
			andView.addObject("msg", "Depaerment Not Existed");
			return null;
		}
		return true;

	}

	public List<AllPersonDto> getAllPersonsData() {
		List<Person> persons = personRepository.findAll();
		List<AllPersonDto> allPersonDtos = new ArrayList<>();

		for (Person person : persons) {
			Department department = person.getDepartment();

			AllPersonDto dto = new AllPersonDto(person.getId(), person.getName(),
					(department != null) ? department.getDeptname() : null, person.getEmail(), person.getPassword());

			allPersonDtos.add(dto);
		}

		return allPersonDtos;
	}

	public Person getPersonById(Long id) {
		Person byId = personRepository.getReferenceById(id);
		return byId;
	}

	public AllPersonDto getEditPersonDtoById(Long personId) {
		Person person = personRepository.findById(personId).orElse(null);

		if (person != null) {

			return new AllPersonDto(person.getId(), person.getName(), person.getDepartment().getDeptid(),
					person.getEmail(), person.getPassword());
		}

		return null; // or throw an exception, depending on your requirements
	}

	public void updatePerson(AllPersonDto editPersonDto) {
		Person person = personRepository.findById(editPersonDto.getPersonId()).orElse(null);

		if (person != null) {
			person.setName(editPersonDto.getPersonName());
			// Assuming the department name is available through the person's department
			Optional<Department> findById = departmentRepository.findById(editPersonDto.getDepartId());
			if (findById.isPresent()) {
				person.setDepartment(findById.get());
				person.setEmail(editPersonDto.getEmail());
				person.setPassword(editPersonDto.getPassword());

				personRepository.save(person);

			}

		}
	}

	public void deletePersonById(Long personId) {
		personRepository.deleteById(personId);
	}

	public boolean check_cred(@Valid UserCred cred, HttpSession session) {
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
