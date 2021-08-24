package com.shanepaulus.challenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Shane Paulus
 * <p>
 * Date Created : 23-Aug-2021.
 */

@SpringBootApplication
@ComponentScan({ "com.shanepaulus.challenge.api", "com.shanepaulus.challenge.service", "com.shanepaulus.challenge.config", "com.shanepaulus.challenge.repo" })
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
