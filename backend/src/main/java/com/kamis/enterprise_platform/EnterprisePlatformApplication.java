package com.kamis.enterprise_platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication(scanBasePackages = "com.kamis")
@ConfigurationPropertiesScan
public class EnterprisePlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnterprisePlatformApplication.class, args);
	}

}
