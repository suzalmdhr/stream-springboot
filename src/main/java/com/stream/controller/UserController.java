package com.stream.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.stream.dto.AuthRequest;
import com.stream.entity.User;
import com.stream.service.JwtService;
import com.stream.service.UserService;
@CrossOrigin(origins ="http://127.0.0.1:5173")
@RestController
public class UserController {
	
	
	@Autowired
	private UserService userService;
	
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	
	@PostMapping("/user/save")
	public ResponseEntity<User> saveUser(@RequestBody User user){
		System.out.println("User is " + user.getUsername());
		System.out.println("Whole user ko bean is " + user);
		
		
		
		
		User user1 = this.userService.createUser(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(user1);
		
		
	}
	
	
	@PostMapping("/authenticate")
	public ResponseEntity<String> AuthToken(@RequestBody AuthRequest authRequest) {
		
		try {
			Authentication authentication= authenticationManager.authenticate
					(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
			
			
			if(authentication.isAuthenticated()) {
				 String token = jwtService.generateToken(authRequest.getUsername());
				 return ResponseEntity.ok(token);
			}
			else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username and password");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username and password");
		}
		
		
		
		
		
	}
	
	
	
	
	
	
	

}
