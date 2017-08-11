package com.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.controller")
@SpringBootApplication
public class AddressqualityvalidationApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(AddressqualityvalidationApplication.class);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(AddressqualityvalidationApplication.class, args);
	}
}