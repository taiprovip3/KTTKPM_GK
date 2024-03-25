package com.example.demo1.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo1.entity.Role;
import com.example.demo1.entity.User;
import com.example.demo1.entity.UserDTO;
import com.example.demo1.entity.UserPrincipal;
import com.example.demo1.repository.UserRepository;

@Service
@Transactional
public class UserServicesImpl implements UserServices{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleServices roleServices;
	
	@Autowired
    private PasswordEncoder bcryptEncoder;
	
	@Override
	public User saveUser(UserDTO u) {
		
		Role memberRole = roleServices.getMemberRole(Long.valueOf(1));
		System.out.println(memberRole);
		
		Set<Role> roles = new HashSet<>();
		roles.add(memberRole);
		
		User user = new User();
		user.setUsername(u.getUsername());
		user.setPassword(bcryptEncoder.encode(u.getPassword()));
		user.setRoles(roles);
		System.out.println(user);
		
		return userRepository.save(user);
	}

	@Override
	public UserPrincipal getUserByUsername(String username) {
		User user = userRepository.findUserByUsername(username);
		UserPrincipal userPrincipal = new UserPrincipal();
		if(user != null) {
			Set<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>();
			if(user.getRoles() != null) {
				user.getRoles().forEach(role -> {
					GrantedAuthority authority = new SimpleGrantedAuthority(role.getName());
					grantedAuthorities.add(authority);
				});
			}
			userPrincipal.setId(user.getId());
			userPrincipal.setUsername(user.getUsername());
			userPrincipal.setPassword(user.getPassword());
			userPrincipal.setAuthorities(grantedAuthorities);
		}
		return userPrincipal;
	}

	@Override
	public User getUserByUserName(String username) {
		return userRepository.findUserByUsername(username);
	}

	@Override
	@Cacheable(value = "users", key = "'allUsers'")
	public List<User> getUsers() {
		return userRepository.findAll();
	}
	
}