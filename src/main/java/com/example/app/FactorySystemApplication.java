package com.example.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.app")
public class FactorySystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(FactorySystemApplication.class, args);
	}

}
