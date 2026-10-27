package com.example.Proyecto_Final_Progra3.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@Slf4j
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        try {
            return http
                    .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                    .csrf(csrf -> csrf.disable())
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .exceptionHandling(ex -> ex
                            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                            .accessDeniedHandler(customAccessDeniedHandler)
                    )
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/error").permitAll()
                            .requestMatchers("/api/v1/auth/**", "/api/v1/pagos/webhook/**").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/v1/habilidades").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/v1/bootcamps", "/api/v1/bootcamps/*", "/api/v1/bootcamps/disponibles", "/api/v1/bootcamps/modalidad/*").permitAll()
                            // Solo lectura de feed y detalle individual público (evita wildcard /** que exponía /postulaciones)
                            .requestMatchers(HttpMethod.GET, "/api/v1/vacantes", "/api/v1/vacantes/*").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/v1/freelance/proyectos", "/api/v1/freelance/proyectos/*").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/v1/perfiles/*/calificaciones").permitAll()
                            
                            // Rutas protegidas con roles estrictos
                            .requestMatchers("/api/v1/empresas/**").hasRole("EMPRESA")
                            .requestMatchers("/api/v1/vacantes/**").hasAnyRole("EMPRESA", "RECRUITER", "POSTULANTE", "EMPLOYEE", "FREELANCER")
                            .requestMatchers("/api/v1/recruiter/**").hasAnyRole("EMPRESA", "RECRUITER")
                            .requestMatchers("/api/v1/freelancer/**", "/api/v1/freelance/**", "/api/v1/portfolio/**").hasAnyRole("EMPLOYEE", "FREELANCER", "CLIENTE")
                            .requestMatchers("/api/v1/instructores/**", "/api/v1/bootcamps/**", "/api/v1/calificaciones/**", "/api/v1/certificados/**", "/api/v1/inscripciones/**").hasRole("INSTRUCTOR")
                            .requestMatchers("/api/v1/client/**").hasRole("CLIENTE")
                            .anyRequest().authenticated()
                    )
                    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                    .build();
        } catch (Exception e) {
            log.error("Error al configurar la cadena de filtros de seguridad: {}", e.getMessage(), e);
            throw new IllegalStateException("No se pudo inicializar la configuración de Spring Security", e);
        }
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("http://localhost:4200", "http://localhost:*", "https://*.worklink.com"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept", "X-Requested-With", "Origin"));
        config.setExposedHeaders(List.of("Authorization", "Content-Disposition"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
        try {
            return config.getAuthenticationManager();
        } catch (Exception e) {
            log.error("Error al obtener el AuthenticationManager: {}", e.getMessage(), e);
            throw new IllegalStateException("No se pudo obtener el AuthenticationManager", e);
        }
    }

    @Bean
    public org.springframework.security.crypto.password.PasswordEncoder passwordEncoder() {
        return new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
    }
}
