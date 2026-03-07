package com.javacore.spring_api_login;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SpringApiLoginApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringApiLoginApplication.class, args);
	}

}