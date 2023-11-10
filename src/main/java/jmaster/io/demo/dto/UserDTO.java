package jmaster.io.demo.dto;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data

public class UserDTO {
	
	private int id;
	
	@NotBlank(message ="{not.blank}")
	private String name;
	
	private String avatarURL;
	
	//@NotBlank(message ="{not.blank}")
	private String username;// unique
	
	private String password;
	
	//many to one
	 private DepartmentDTO department;
	 
	private List<String> roles;

	 @DateTimeFormat(pattern = "dd/MM/yyyy")
	 @JsonFormat(pattern = "dd/MM/yyyy" , timezone = "Asia/Ho_Chi_Minh")
	 private Date birthdate;
	 
	 @JsonIgnore
	private MultipartFile file;
	
	
}
