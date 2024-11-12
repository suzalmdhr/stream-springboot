package com.stream.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.stream.filter.JwtAuthFilter;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class MyConfig {
	
	@Autowired
	private JwtAuthFilter jwtAuthFilter;
	
	
	
	  @Bean
	    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
	        return config.getAuthenticationManager();
	    }
	  
	  @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
	  
	  @Bean
	  public AuthenticationProvider authenticationProvider() {
		  DaoAuthenticationProvider authProvider =new DaoAuthenticationProvider();
		  authProvider.setUserDetailsService(userDetailsService());
		  authProvider.setPasswordEncoder(passwordEncoder());
		  return authProvider;
	  }
	  
	  @Bean
	  public UserDetailsService userDetailsService() {
		  
		  return new UserInfoUserDetailsService();
	  }
	  
	  
	 @Bean
	 public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		  return http.
		  csrf().disable()
		  .authorizeHttpRequests()
		  .requestMatchers("/api/v1/videos/stream/range/**")
		  .authenticated()
		  .anyRequest()
		  .permitAll()
		  .and()
		  .formLogin()
		  .and()
		  .build();
		 
//		  .exceptionHandling()
//		  .authenticationEntryPoint((request,response,authException) -> {
//			  response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Unauthorized");
//		  })
//		  .and()
		  
		  
	  }
	  
	  
	  

}
