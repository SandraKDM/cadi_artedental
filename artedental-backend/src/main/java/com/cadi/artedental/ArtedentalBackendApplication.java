package com.cadi.artedental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ArtedentalBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArtedentalBackendApplication.class, args);
	}

}
