package com.kamis.enterprise_platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.kamis")
public class EnterprisePlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnterprisePlatformApplication.class, args);
	}

}
