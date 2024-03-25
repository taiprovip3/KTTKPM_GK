package com.example.demo1.service;


import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo1.entity.Role;
import com.example.demo1.repository.RoleRepository;

@Service
@Transactional
public class RoleServicesImpl implements RoleServices {
	
	@Autowired
	private RoleRepository roleRepository;

	@Override
	public Role getMemberRole(Long id) {
		return roleRepository.getById(id);
	}

	@Override
	public Role saveRole(Role role) {
		return roleRepository.save(role);
	}
}