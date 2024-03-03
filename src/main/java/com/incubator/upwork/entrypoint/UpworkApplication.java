package com.incubator.upwork.entrypoint;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("com.incubator.upwork.data.repository")
@EntityScan("com.incubator.upwork.data.model")
@ComponentScan(value = {"com.incubator.upwork.config","com.incubator.upwork.controller","com.incubator.upwork.services"})
@SpringBootApplication
public class UpworkApplication {

	public static void main(String[] args) {
		SpringApplication.run(UpworkApplication.class, args);
		System.out.println("Main");
	}

}
