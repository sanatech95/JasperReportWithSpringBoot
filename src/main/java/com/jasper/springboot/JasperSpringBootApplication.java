package com.jasper.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.jasper.springboot*")
public class JasperSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(JasperSpringBootApplication.class, args);
	}

}
