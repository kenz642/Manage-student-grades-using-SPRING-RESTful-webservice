package jmaster.io.demo.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jmaster.io.demo.dto.AvgScoreByCourse;
import jmaster.io.demo.dto.PageDTO;
import jmaster.io.demo.dto.ResponseDTO;
import jmaster.io.demo.dto.ScoreDTO;
import jmaster.io.demo.dto.SearchScoreDTO;
import jmaster.io.demo.service.ScoreService;

@RestController
@RequestMapping("/score")
public class ScoreController {
	@Autowired 
	ScoreService scoreService;
	
	@PostMapping("")
	public ResponseDTO<Void> create(@RequestBody @Valid ScoreDTO scoreDTO) {
		scoreService.create(scoreDTO);
		return ResponseDTO.<Void>builder().status(200).msg("ok").build();
	}
	
	@DeleteMapping("/") // ?id =1000
	public ResponseDTO<Void> delete(@RequestParam("id") int id) {
		scoreService.delete(id);
		return ResponseDTO.<Void>builder().status(200).msg("ok").build();
	}
	
	@GetMapping("/")
	@ResponseStatus(code = HttpStatus.OK) //HTTP STUTUS CODE
	public ResponseDTO<ScoreDTO> get(@RequestParam("id") int id){
		return 	 ResponseDTO.<ScoreDTO>builder().status(200).data(scoreService.getById(id)).build();
	}

	@PutMapping("/")
	public ResponseDTO<ScoreDTO> edit(@ModelAttribute @Valid ScoreDTO scoreDTO){	
		scoreService.update(scoreDTO);
		return 	 ResponseDTO.<ScoreDTO>builder().status(200).data(scoreDTO).build();
	}
	
	@PostMapping("/search")
	public ResponseDTO<PageDTO<List<ScoreDTO>>> search(@RequestBody @Valid SearchScoreDTO searchDTO) {
		PageDTO<List<ScoreDTO>> pageScore = scoreService.search(searchDTO);
		return ResponseDTO.<PageDTO<List<ScoreDTO>>>builder().status(200).data(pageScore).build();
	}
	
	@GetMapping("/avg-score-by-course")
	public ResponseDTO<List<AvgScoreByCourse>> avgScoreByCourse(){
		return ResponseDTO.<List<AvgScoreByCourse>>builder().status(200)
				.data(scoreService.avgScoreByCourses()).build();
	}
}
