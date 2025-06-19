package com.asyncorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AsyncOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(AsyncOrderApplication.class, args);
	}

}
