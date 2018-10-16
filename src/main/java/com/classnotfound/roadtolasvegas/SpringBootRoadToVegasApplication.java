package com.classnotfound.roadtolasvegas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootRoadToVegasApplication {

	public static void main(String[] args) {
        System.setProperty("spring.jackson.serialization.INDENT_OUTPUT", "true");
		SpringApplication.run(SpringBootRoadToVegasApplication.class, args);
	}
}
