package jmaster.io.demo.entity;


import java.util.Date;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Data
@Table(name = "user")
@Entity

// table user
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;// PK
	
	//@OneToMany(mappedBy = "user")
	//private List<UserRole> roles;
	
	//ap dung cho bang chi co 2 cot
	@ElementCollection
	@CollectionTable(name= "user_role" , joinColumns = @JoinColumn(name="user_id"))
	@Column(name="role")
	private List<String> roles;
	
	@ManyToOne
	private Department department;
	
	private int age;
	
	private String name;// uname
	
	private String avatarURL;
	
	@Column(unique = true)
	private String username;// unique
	
	private String password;// 
	
	@Temporal(TemporalType.DATE)
	private Date birthdate;
	
	private String email;// 


	
}
