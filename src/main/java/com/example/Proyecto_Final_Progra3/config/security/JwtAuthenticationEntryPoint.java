package com.example.Proyecto_Final_Progra3.config.security;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import com.example.Proyecto_Final_Progra3.dto.responsedto.ErrorResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint{
    private final ObjectMapper objectMapper =  new ObjectMapper().registerModule(new JavaTimeModule());

    @Override
     public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // HTTP 401
        ErrorResponseDTO error = new ErrorResponseDTO(
                "No autorizado",
                List.of("Se requiere autenticación mediante token JWT válido: " + authException.getMessage()),
                LocalDate.now()
        );
        objectMapper.writeValue(response.getOutputStream(), error);
    }
}
