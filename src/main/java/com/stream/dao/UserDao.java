package com.stream.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stream.entity.User;

public interface UserDao extends JpaRepository<User, Integer> {

	
	Optional<User> findByUsername(String username);
}
