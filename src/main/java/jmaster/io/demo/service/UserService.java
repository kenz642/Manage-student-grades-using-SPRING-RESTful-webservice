package jmaster.io.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import jmaster.io.demo.dto.PageDTO;
import jmaster.io.demo.dto.SearchDTO;
import jmaster.io.demo.dto.UserDTO;
import jmaster.io.demo.entity.User;
import jmaster.io.demo.repository.UserRepo;

@Service // tao bean : new Userservice , ql boi SpringController
public class UserService implements UserDetailsService {
	@Autowired
	UserRepo userRepo;

	@Transactional
	public void create(UserDTO userDTO) {
		// convert userdto => user
		/*
		 * User user = new User();
		 * user.setName(userDTO.getName());
		 * user.setAvatarURL(userDTO.getAvatarURL());
		 * user.setUsername(userDTO.getUsername());
		 * user.setPassword(userDTO.getPassword());
		 */
		User user = new ModelMapper().map(userDTO, User.class);
		user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
		userRepo.save(user);
	}

	@Transactional
	public void delete(int id) {
		userRepo.deleteById(id);
	}

	@Transactional
	public void update(UserDTO userDTO) {
		// check
		User currenUser = userRepo.findById(userDTO.getId()).orElse(null);
		if (currenUser != null) {
			currenUser.setName(userDTO.getName());

		}
		userRepo.save(currenUser);
	}

	public void updatePassword(UserDTO userDTO) {
		// check
		User currenUser = userRepo.findById(userDTO.getId()).orElse(null);
		if (currenUser != null) {
			currenUser.setPassword(new BCryptPasswordEncoder().encode(userDTO.getPassword()));
			
			userRepo.save(currenUser);
		}

	}

	public PageDTO<List<UserDTO>> searchName(SearchDTO searchDto) {
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

		PageRequest pageRequest = PageRequest.of(searchDto.getCurrenPage(), searchDto.getSize(), sortBy);

		Page<User> page = userRepo.searchByName("%" + searchDto.getKeyword() + "%", pageRequest);

		PageDTO<List<UserDTO>> pageDTO = new PageDTO<>();
		pageDTO.setTotalPages(page.getTotalPages());
		pageDTO.setTotalElements(page.getTotalElements());

		List<User> users = page.getContent();
		List<UserDTO> userDTOs = page.get().map(u -> convert(u)).collect(Collectors.toList());

		// T: List<userDTO>
		pageDTO.setData(userDTOs);

		return pageDTO;
	}

	
	private UserDTO convert(User user) {
		return new ModelMapper().map(user, UserDTO.class);
	}

	public UserDTO getById(int id) {
		User user = userRepo.findById(id).orElse(null);
		if (user != null) {
			return convert(user);
		}
		return null;
	}

	@Transactional
	public List<UserDTO> getAll() {
		List<User> userList = userRepo.findAll();

		/*
		 * List<UserDTO> userDTOs = new ArrayList<>(); for(User user : userList) {
		 * userDTOs.add(convert(user)); } return userDTOs;
		 */
		// java 8;
		return userList.stream().map(u -> convert(u)).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User userEntity = userRepo.findByUsername(username);
		if(userEntity == null) {
			throw new UsernameNotFoundException("Not found");
		}
		//convert userentity -> userdetails
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		//chuyen vai tro ve quyen
		for(String role : userEntity.getRoles()) {
			authorities.add(new SimpleGrantedAuthority(role));
		}
		return new org.springframework.security.core.userdetails.User(username, userEntity.getPassword(), authorities);
	}
}
