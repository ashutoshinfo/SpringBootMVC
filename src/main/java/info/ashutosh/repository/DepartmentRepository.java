package info.ashutosh.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import info.ashutosh.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

	Department findByDeptname(String departName);

}
