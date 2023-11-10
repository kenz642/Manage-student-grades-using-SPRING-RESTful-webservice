package jmaster.io.demo.controller;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
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

import jmaster.io.demo.dto.DepartmentDTO;
import jmaster.io.demo.dto.PageDTO;
import jmaster.io.demo.dto.ResponseDTO;
import jmaster.io.demo.dto.SearchDTO;
import jmaster.io.demo.service.DepartmentService;

@RestController
@RequestMapping("/department")
public class DepartmentController {
	@Autowired
	DepartmentService departmentService;


	@PostMapping("/")
	public ResponseDTO<Void> create(@ModelAttribute @Valid DepartmentDTO departmentDTO) {
		departmentService.create(departmentDTO);
		return ResponseDTO.<Void>builder().status(200).msg("ok").build();
	}
	
	
	// Nếu đẩy lên dạng json sử dụng @requestBody
	//Json không upload được file
	@PostMapping("/json")
	public ResponseDTO<Void> createNewJson(@RequestBody @Valid DepartmentDTO departmentDTO) {
		departmentService.create(departmentDTO);
		return ResponseDTO.<Void>builder().status(200).msg("ok").build();
	}
	
	@DeleteMapping("/") // ?id =1000
	public ResponseDTO<Void> delete(@RequestParam("id") int id) {
		departmentService.delete(id);
		return ResponseDTO.<Void>builder().status(200).msg("ok").build();
	}
	
	@GetMapping("/")
	@ResponseStatus(code = HttpStatus.OK) //HTTP STUTUS CODE
	//@Secured("ROLE_ADMIN") // ROLE
	//@RolesAllowed("ROLE_ADMIN")
	@PreAuthorize("isAuthenticated")
	public ResponseDTO<DepartmentDTO> get(@RequestParam("id") int id){
		return 	 ResponseDTO.<DepartmentDTO>builder().status(200).data(departmentService.getById(id)).build();
	}

	@PutMapping("/")
	public ResponseDTO<DepartmentDTO> edit(@ModelAttribute @Valid DepartmentDTO departmentDTO){	
		departmentService.update(departmentDTO);
		return 	 ResponseDTO.<DepartmentDTO>builder().status(200).data(departmentDTO).build();
	}
	
	@PostMapping("/search")
	public ResponseDTO<PageDTO<List<DepartmentDTO>>> search(@ModelAttribute @Valid SearchDTO searchDTO) {
		PageDTO<List<DepartmentDTO>> pageDepartment = departmentService.search(searchDTO);
		return ResponseDTO.<PageDTO<List<DepartmentDTO>>>builder().status(200).data(pageDepartment).build();
	}
}
