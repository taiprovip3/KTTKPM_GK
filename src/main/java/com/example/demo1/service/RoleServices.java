package com.example.demo1.service;

import com.example.demo1.entity.Role;

public interface RoleServices {
	public Role getMemberRole(Long id);
	public Role saveRole(Role role);
}