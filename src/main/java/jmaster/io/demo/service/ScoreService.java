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

import jmaster.io.demo.dto.AvgScoreByCourse;
import jmaster.io.demo.dto.PageDTO;
import jmaster.io.demo.dto.ScoreDTO;
import jmaster.io.demo.dto.SearchScoreDTO;
import jmaster.io.demo.entity.Course;
import jmaster.io.demo.entity.Score;
import jmaster.io.demo.entity.Student;
import jmaster.io.demo.repository.CourseRepo;
import jmaster.io.demo.repository.ScoreRepo;
import jmaster.io.demo.repository.StudentRepo;

public interface ScoreService {
	void create(ScoreDTO scoreDTO);

	void update(ScoreDTO scoreDTO);

	void delete(int id);

	ScoreDTO getById(int id);

	PageDTO<List<ScoreDTO>> search(SearchScoreDTO searchDTO);
	
	List<AvgScoreByCourse> avgScoreByCourses();
}

@Service
class ScoreServiceIplm implements ScoreService {
	@Autowired
	private ScoreRepo scoreRepo;
	
	@Autowired
	private StudentRepo studentRepo;
	
	@Autowired CourseRepo courseRepo;

	@Override
	public void create(ScoreDTO scoreDTO) {
		Score score = new ModelMapper().map(scoreDTO, Score.class);
		scoreRepo.save(score);
	}

	@Transactional
	@Override
	public void update(ScoreDTO scoreDTO) {
		Score score = scoreRepo.findById(scoreDTO.getId()).orElseThrow(NoResultException::new);
		score.setScore(scoreDTO.getScore());
		scoreRepo.save(score);

		Student student = studentRepo.findById(scoreDTO.getStudent().getUser().getId())
				.orElseThrow(NoResultException::new);
		score.setStudent(student);
		
		Course course = courseRepo.findById(scoreDTO.getCourse().getId())
				.orElseThrow(NoResultException::new);
		score.setCourse(course);
	}
	@Transactional
	@Override
	public void delete(int id) {
		scoreRepo.deleteById(id);
		
	}

	@Override
	public ScoreDTO getById(int id) {
		Score score = scoreRepo.findById(id).orElseThrow(NoResultException::new);
			return convert(score);
		}

private ScoreDTO convert(Score score) {
			return new ModelMapper().map(score, ScoreDTO.class);
	}

	@Override
	public PageDTO<List<ScoreDTO>> search(SearchScoreDTO searchDTO) {
		Sort sortBy = Sort.by("id").ascending();

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
		
		Page<Score> page = null;
		if(searchDTO.getCourseId() != null) {
			page = scoreRepo.searchByCourse(searchDTO.getCourseId(), pageRequest);
		}else if (searchDTO.getStudentId() != null) {
			page = scoreRepo.searchByStudent(searchDTO.getStudentId(), pageRequest);
		}
		else {
			page = scoreRepo.findAll(pageRequest);
		}
			
		PageDTO<List<ScoreDTO>> pageDTO = new PageDTO<>();
		pageDTO.setTotalPages(page.getTotalPages());
		pageDTO.setTotalElements(page.getTotalElements());

		List<ScoreDTO> scoreDTOs =page.get().map(d -> convert(d)).collect(Collectors.toList());
		pageDTO.setData(scoreDTOs);
		
		return pageDTO;
	}

	@Override
	public List<AvgScoreByCourse> avgScoreByCourses() {
		return scoreRepo.avgScoreByCourse();
	}


}
