package com.ii.testautomation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
public class InvictaTestAutomationProjectApplication {
	public static void main(String[] args) {
		SpringApplication.run(InvictaTestAutomationProjectApplication.class, args);

	}
}
