package com.sqc.acedemy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class AcedemyApplication {

	public static void main(String[] args) {
		SpringApplication.run(AcedemyApplication.class, args);
	}

}
