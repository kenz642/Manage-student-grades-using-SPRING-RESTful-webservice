package jmaster.io.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import jmaster.io.demo.dto.CourseDTO;
import jmaster.io.demo.dto.PageDTO;
import jmaster.io.demo.dto.SearchDTO;
import jmaster.io.demo.entity.Course;
import jmaster.io.demo.repository.CourseRepo;

public interface CourseService {
	void create(CourseDTO courseDTO);

	void update(CourseDTO courseDTO);

	void delete(int id);

	CourseDTO getById(int id);

	PageDTO<List<CourseDTO>> search(SearchDTO searchDTO);
}

@Service
class CourseServiceIplm implements CourseService {
	@Autowired
	private CourseRepo courseRepo;

	@Override
	public void create(CourseDTO courseDTO) {
		Course course = new ModelMapper().map(courseDTO, Course.class);
		courseRepo.save(course);
	}

	@Transactional
	@Override
	public void update(CourseDTO courseDTO) {
		Course course = courseRepo.findById(courseDTO.getId()).orElse(null);

		if (course != null) {
			course.setName(courseDTO.getName());
			courseRepo.save(course);
		}
	}

	@Transactional
	@Override
	public void delete(int id) {
		courseRepo.deleteById(id);
	}

	@Override
	public CourseDTO getById(int id) {
		Course course = courseRepo.findById(id).orElseThrow(NoResultException::new);

		return convert(course);
	}

	private CourseDTO convert(Course course) {
		return new ModelMapper().map(course, CourseDTO.class);
	}

	@Override
	public PageDTO<List<CourseDTO>> search(SearchDTO searchDTO) {
		Sort sortBy = Sort.by("name").ascending();

		if (StringUtils.hasText(searchDTO.getSortedField())) {
			sortBy = Sort.by(searchDTO.getSortedField()).ascending();
		}
		if (searchDTO.getSize() == null) {
			searchDTO.setSize(5);
		}
		if (searchDTO.getCurrenPage() == null) {
			searchDTO.setCurrenPage(0);
		}
		if(searchDTO.getKeyword() == null) {
			searchDTO.setKeyword("");
		}
		
		PageRequest pageRequest = PageRequest.of(searchDTO.getCurrenPage(), searchDTO.getSize(), sortBy);
		
		Page<Course> page = courseRepo.searchName("%" + searchDTO.getKeyword() + "%", pageRequest);
		
		PageDTO<List<CourseDTO>> pageDTO = new PageDTO<>();
		pageDTO.setTotalPages(page.getTotalPages());
		pageDTO.setTotalElements(page.getTotalElements());

		//List<Department>
		List<CourseDTO> courseDTOs =page.get().map(d -> convert(d)).collect(Collectors.toList());
		
		//T: List<userDTO>
		pageDTO.setData(courseDTOs);
		
		return pageDTO;
	}

}
