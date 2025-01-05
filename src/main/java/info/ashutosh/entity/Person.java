package info.ashutosh.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Person {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private String email;
	private String password;

	public Person() {
	}

	public Person(String name, String email, String password, Department department) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		this.department = department;
	}

	public Person(Person user) {
		this.id = user.id;
		this.name = user.name;
		this.password = user.password;
		this.email = user.email;

	}

	@ManyToOne
	@JoinColumn(name = "deptid_fk")
	private Department department;

}
