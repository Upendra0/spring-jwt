package com.upendra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableMethodSecurity
@EnableTransactionManagement
public class SpringJwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringJwtApplication.class, args);
	}

}
