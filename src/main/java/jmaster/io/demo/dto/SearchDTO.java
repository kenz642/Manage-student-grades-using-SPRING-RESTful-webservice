package jmaster.io.demo.dto;

import lombok.Data;

@Data
public class SearchDTO {
	private String keyword;
	private Integer currenPage ;
	private Integer size ;
	private String sortedField;
}
