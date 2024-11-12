package com.stream.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.stream.dao.UserDao;
import com.stream.entity.User;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public User createUser(User user) {
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
	return	this.userDao.save(user) ;
		
	}

	@Override
	public User getUser(int userId) {
	return	this.userDao.findById(userId).orElseThrow(() -> new RuntimeException("No such user"));
		
	}

	

}
