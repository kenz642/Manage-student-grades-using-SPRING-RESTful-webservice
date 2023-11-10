package jmaster.io.demo.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import lombok.Data;

@Entity
@Data
public class Student {
	@Id
	private int userId; //user_id
	
	@OneToOne(cascade = CascadeType.ALL
			,fetch = FetchType.EAGER)
	@PrimaryKeyJoinColumn
	@MapsId // copy id của user set cho id của student
	private User user; //user_id
	
	private String studentCode;
	
	@OneToMany(mappedBy = "student")
	private List<Score> scores;
}
