package com.HRMS.QuickDines.Config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;

import org.springframework.web.cors.CorsConfigurationSource;

import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;


    @Bean
    public SecurityFilterChain securityFilterChain
            (HttpSecurity http) throws Exception {

        http

                .csrf(csrf -> csrf.disable())

                .cors(Customizer.withDefaults())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth


                        // Authentication APIs
                        .requestMatchers(
                                "/api/auth/**"
                        ).permitAll()


                        // Swagger
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()


                        // WebSocket
                        .requestMatchers(
                                "/ws/**"
                        ).permitAll()


                        // Health Check
                        .requestMatchers(
                                "/actuator/**"
                        ).permitAll()


                        // Public APIs
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/public/**"
                        ).permitAll()


                        // ADMIN

                        .requestMatchers(
                                "/api/admin/**"
                        ).hasRole("ADMIN")


                        // HR

                        .requestMatchers(
                                "/api/hr/**"
                        ).hasRole("HR")


                        // EMPLOYEE

                        .requestMatchers(
                                "/api/employee/**"
                        ).hasRole("EMPLOYEE")


                        // SALES EXECUTIVE

                        .requestMatchers(
                                "/api/sales/**"
                        ).hasRole("SALES")


                        // FINANCE

                        .requestMatchers(
                                "/api/finance/**"
                        ).hasRole("FINANCE")


                        // SUPER ADMIN

                        .requestMatchers(
                                "/api/super-admin/**"
                        ).hasRole("SUPER_ADMIN")


                        .anyRequest()
                        .authenticated()

                )

                .addFilterBefore(
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class
                );


        return http.build();

    }


    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();

    }


    @Bean
    public AuthenticationManager authenticationManager
            (AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();

    }



    @Bean
    public CorsConfigurationSource
    corsConfigurationSource() {


        CorsConfiguration configuration =
                new CorsConfiguration();


        configuration.setAllowedOrigins(
                List.of(

                        "http://localhost:5173",

                        "https://your-vercel-app.vercel.app"

                ));


        configuration.setAllowedMethods(
                Arrays.asList(

                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "PATCH",
                        "OPTIONS"

                ));


        configuration.setAllowedHeaders(
                List.of("*")
        );


        configuration.setAllowCredentials(true);


        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();


        source.registerCorsConfiguration(
                "/**",
                configuration
        );


        return source;

    }


}