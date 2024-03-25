package com.example.demo1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo1.entity.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	public User findUserByUsername(String username);
}
