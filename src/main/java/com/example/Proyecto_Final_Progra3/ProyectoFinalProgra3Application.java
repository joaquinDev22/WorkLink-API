package com.example.Proyecto_Final_Progra3;

import com.example.Proyecto_Final_Progra3.config.security.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class ProyectoFinalProgra3Application {

	public static void main(String[] args) {
		SpringApplication.run(ProyectoFinalProgra3Application.class, args);
	}

}
