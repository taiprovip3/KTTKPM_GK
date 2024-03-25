package com.example.demo1.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo1.entity.User;
import com.example.demo1.entity.UserDTO;
import com.example.demo1.entity.UserPrincipal;

public interface UserServices {
	public User saveUser(UserDTO u);
	public UserPrincipal getUserByUsername(String username);
	public User getUserByUserName(String username);
	public List<User> getUsers();
}
