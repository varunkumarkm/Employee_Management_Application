package com.employee.management.app;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EmployeeManagementApp {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeManagementApp.class, args);
	}
	
	@Bean
	public ModelMapper model() {
	    return new ModelMapper();
	}
}
