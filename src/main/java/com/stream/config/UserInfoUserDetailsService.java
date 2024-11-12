package com.stream.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.stream.dao.UserDao;
import com.stream.entity.User;

@Component
public class UserInfoUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserDao userDao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	Optional<User> user = this.userDao.findByUsername(username);
	
	return user.map(UserInfoDetails::new)
			.orElseThrow(() -> new UsernameNotFoundException("user not found"));
		
	}

}
