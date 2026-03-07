package com.javacore.spring_api_login;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Slf4j
@SpringBootApplication
@EnableJpaAuditing
public class SpringApiLoginApplication {

	public static void main(String[] args) {
		log.info("Iniciando aplicação...");
		SpringApplication.run(SpringApiLoginApplication.class, args);
	}

}