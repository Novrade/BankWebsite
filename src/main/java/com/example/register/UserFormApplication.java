package com.example.register;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication()
public class UserFormApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserFormApplication.class, args);
	}

}
