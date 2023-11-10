package jmaster.io.demo.dto;

import lombok.Data;

@Data
public class StudentDTO {
	private int userId; 

	private UserDTO user; //user_id
	private String studentCode;
}
