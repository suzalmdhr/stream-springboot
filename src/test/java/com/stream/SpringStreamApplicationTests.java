package com.stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.stream.service.VideoService;

@SpringBootTest
class SpringStreamApplicationTests {
	
	@Autowired
	private VideoService videoService;

	@Test
	void contextLoads() {
		this.videoService.processVid("cc0d97da-a80d-4481-bc0e-d3b331dac909");
	}
	
	

}
