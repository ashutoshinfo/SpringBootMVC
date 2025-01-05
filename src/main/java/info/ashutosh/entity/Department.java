package info.ashutosh.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Department {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long deptid;

	private String deptname;

	@OneToMany(mappedBy = "department")
	private List<Person> person;

	@Override
	public String toString() {
		return "Department [deptid=" + deptid + ", deptname=" + deptname + ", person=" + person + "]";
	}

}
