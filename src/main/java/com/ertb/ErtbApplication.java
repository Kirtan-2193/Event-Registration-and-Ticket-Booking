package com.ertb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ErtbApplication {

	public static void main(String[] args) {
		SpringApplication.run(ErtbApplication.class, args);
	}

}
