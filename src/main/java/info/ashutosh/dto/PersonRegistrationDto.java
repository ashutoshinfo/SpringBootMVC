package info.ashutosh.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonRegistrationDto {

	private String name;
	private String departId;
	private String email;
	private String password;
	@Override
	public String toString() {
		return "PersonRegistrationDto [name=" + name + ", departId=" + departId + ", email=" + email + ", password="
				+ password + "]";
	}
	
	

}
