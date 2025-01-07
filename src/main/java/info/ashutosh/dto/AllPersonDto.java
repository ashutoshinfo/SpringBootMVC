package info.ashutosh.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AllPersonDto {

	private Long id;
	private String personName;
	private String departName;
	private Long departId;
	private String email;
	private String password;
	
	
	public AllPersonDto() {
	}

	public AllPersonDto(Long id, String personName, Long long1, String email, String password) {
		super();
		this.id = id;
		this.personName = personName;
		this.departId = long1;
		this.email = email;
		this.password = password;
	}
	
	public AllPersonDto(Long id, String personName, String long1, String email, String password) {
		super();
		this.id = id;
		this.personName = personName;
		this.departName = long1;
		this.email = email;
		this.password = password;
	}

}
