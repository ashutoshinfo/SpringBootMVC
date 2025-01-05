package info.ashutosh.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AllPersonDto {

	private Long personId;
	private String personName;
	private String departName;
	private Long departId;
	private String email;
	private String password;
	
	
	public AllPersonDto() {
	}

	public AllPersonDto(Long personId, String personName, Long long1, String email, String password) {
		super();
		this.personId = personId;
		this.personName = personName;
		this.departId = long1;
		this.email = email;
		this.password = password;
	}
	
	public AllPersonDto(Long personId, String personName, String long1, String email, String password) {
		super();
		this.personId = personId;
		this.personName = personName;
		this.departName = long1;
		this.email = email;
		this.password = password;
	}

}
