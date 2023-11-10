package jmaster.io.demo.service;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jmaster.io.demo.dto.StudentDTO;
import jmaster.io.demo.entity.Student;
import jmaster.io.demo.repository.StudentRepo;

public interface StudentService {
	void create(StudentDTO studentDTO);
	
	void update(StudentDTO studentDTO);
	
	void delete(int id);
	
	StudentDTO getById(int id);
}

@Service
class StudentServiceIplm implements StudentService{
	@Autowired
	StudentRepo studentRepo;
	
	
	
	@Override
	@Transactional
	public void create(StudentDTO studentDTO) {
		//User user = new ModelMapper().map(studentDTO.getUser(), User.class);
		
		//dung casecade
		Student student = new ModelMapper().map(studentDTO, Student.class);
		//student.setUserId(user.getId());
		//student.setUser(user);
		//student.setStudentCode(studentDTO.getStudentCode());
		
		studentRepo.save(student);
		
	}

	@Override
	public void update(StudentDTO studentDTO) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public StudentDTO getById(int id) {
		Student student = studentRepo.findById(id).orElseThrow(NoResultException::new);
			return convert(student);
	}
	
	
	private StudentDTO convert(Student student) {
		ModelMapper modelMapper =new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		return  modelMapper.map(student, StudentDTO.class);
	}
	
}