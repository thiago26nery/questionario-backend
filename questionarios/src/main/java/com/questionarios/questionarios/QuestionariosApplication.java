package com.questionarios.questionarios;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@SecurityScheme(
		name = "bearerAuth",          // Nome identificador do tipo de segurança
		type = SecuritySchemeType.HTTP, // Diz que é uma segurança via protocolo HTTP
		scheme = "bearer",            // Tipo Bearer Token
		bearerFormat = "JWT"          // Formato do token é JWT
)
public class QuestionariosApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuestionariosApplication.class, args);
	}

}
