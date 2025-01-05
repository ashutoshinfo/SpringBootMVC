package info.ashutosh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import info.ashutosh.entity.Department;
import info.ashutosh.repository.DepartmentRepository;

@Service
public class DepartmentService {

	@Autowired
	DepartmentRepository departmentRepository;

	public List<Department> getAllDepartment() {

		List<Department> findAll = departmentRepository.findAll();

		return findAll;
	}

}
