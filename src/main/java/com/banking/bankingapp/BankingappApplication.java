package com.banking.bankingapp;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Demo Banking Application",
				description = "Backend REST API for the app",
				version = "v1.0",
				contact = @Contact(
						name = "Truong Hoa Giang",
						email = "giangtruonghoa@gmail.com",
						url = "https://github.com/truonghoagiang/bankingApp"
				),
				license = @License(
						name = "Java Spring Boot",
						url = "https://github.com/truonghoagiang/bankingApp"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "Demo Banking Application",
				url = "https://github.com/truonghoagiang/bankingApp"
		)

)
public class BankingappApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingappApplication.class, args);
	}

}
