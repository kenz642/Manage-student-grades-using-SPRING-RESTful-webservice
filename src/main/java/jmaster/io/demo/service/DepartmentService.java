package jmaster.io.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import jmaster.io.demo.dto.DepartmentDTO;
import jmaster.io.demo.dto.PageDTO;
import jmaster.io.demo.dto.SearchDTO;
import jmaster.io.demo.entity.Department;
import jmaster.io.demo.entity.User;
import jmaster.io.demo.repository.DepartmentRepo;

public interface DepartmentService {
	void create(DepartmentDTO departmentDTO);
	
	void update(DepartmentDTO departmentDTO);
	
	void delete(int id);
	
	DepartmentDTO getById(int id);
	
	PageDTO<List<DepartmentDTO>> search(SearchDTO searchDto);
}

@Service
class DepartmentServiceIplm implements DepartmentService{
	@Autowired 
	private DepartmentRepo departmentRepo;
	
	@Autowired
	CacheManager cacheManager;
	
	
	@Override
	@CacheEvict(cacheNames = "department-search", allEntries = true)
	public void create(DepartmentDTO departmentDTO) {
		Department department = new ModelMapper().map(departmentDTO, Department.class);
		departmentRepo.save(department);
		
		Cache cache = cacheManager.getCache("department-search");
		cache.invalidate();
	}

	@Transactional
	@Override
	@Caching(evict = {
			@CacheEvict(cacheNames = "department-search", allEntries = true)
	},
		put = {	@CachePut(cacheNames = "department", key = "#departmentDTO.id")
	})
	public void update(DepartmentDTO departmentDTO) {
		Department department = departmentRepo.findById(departmentDTO.getId()).orElse(null);
		
		if (department != null) {
			department.setName(departmentDTO.getName());
			departmentRepo.save(department);
		}		
	}

	@Transactional
	@Override
	@Caching(evict = {
			@CacheEvict(cacheNames = "department", key = "#id"),
			@CacheEvict(cacheNames = "department-search", allEntries = true)
	})
	public void delete(int id) {
		departmentRepo.deleteById(id);
	}

	
	@Override
	@Cacheable(cacheNames = "department", key = "#id", unless = "#result == null")
	public DepartmentDTO getById(int id) {
		System.out.println("CHUA CO CACHE");
		Department department = departmentRepo.findById(id).orElseThrow(NoResultException::new);
		
		List<User> users = department.getUsers();
		System.out.println(users.size());
		return convert(department);
	}
	
	 DepartmentDTO convert(Department department) {
		return new ModelMapper().map(department, DepartmentDTO.class);
	}


	@Override
	@Cacheable(cacheNames = "department-search")
	public PageDTO<List<DepartmentDTO>> search(SearchDTO searchDto) {
		Sort sortBy = Sort.by("name").ascending();

		if (StringUtils.hasText(searchDto.getSortedField())) {
			sortBy = Sort.by(searchDto.getSortedField()).ascending();
		}
		if (searchDto.getSize() == null) {
			searchDto.setSize(5);
		}
		if (searchDto.getCurrenPage() == null) {
			searchDto.setCurrenPage(0);
		}
		if(searchDto.getKeyword() == null) {
			searchDto.setKeyword("");
		}
		
		PageRequest pageRequest = PageRequest.of(searchDto.getCurrenPage(), searchDto.getSize(), sortBy);
		
		Page<Department> page = departmentRepo.searchName("%" + searchDto.getKeyword() + "%", pageRequest);
		
		PageDTO<List<DepartmentDTO>> pageDTO = new PageDTO<>();
		pageDTO.setTotalPages(page.getTotalPages());
		pageDTO.setTotalElements(page.getTotalElements());

		//List<Department>
		List<DepartmentDTO> departmentDTOs =page.get().map(d -> convert(d)).collect(Collectors.toList());
		
		//T: List<userDTO>
		pageDTO.setData(departmentDTOs);
		
		return pageDTO;
	}
	
}