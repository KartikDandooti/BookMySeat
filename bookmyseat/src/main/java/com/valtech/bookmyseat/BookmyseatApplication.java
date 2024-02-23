package com.valtech.bookmyseat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BookmyseatApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookmyseatApplication.class, args);
	}
}
