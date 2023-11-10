package jmaster.io.demo.dto;

import groovy.transform.EqualsAndHashCode;
import lombok.Data;

@Data
@EqualsAndHashCode(callSuper = true)
public class SearchScoreDTO extends SearchDTO {
	private Integer studentId;
	private Integer courseId;
}
