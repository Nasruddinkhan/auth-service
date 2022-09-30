package com.mypractice.estudy.auth;

import com.mypractice.estudy.auth.util.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
@EnableConfigurationProperties(ApplicationProperties.class)
@ComponentScan("com.mypractice.estudy")
@SpringBootApplication
public class CustomAuthorizatioServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomAuthorizatioServiceApplication.class, args);
	}

}
