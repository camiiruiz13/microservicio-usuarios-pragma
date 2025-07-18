package com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.domain.spi.IUserPersistencePort;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.security.handler.CustomAccessDeniedHandler;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.security.handler.CustomAuthenticationEntryPoint;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.security.jwt.auth.AuthenticationFilter;
import com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.security.jwt.auth.ValidationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.commons.constans.EndPointApi.BASE_URL;
import static com.retoplazoleta.ccamilo.com.microserviciousuarios.infrastructure.commons.constans.EndPointApi.LOGIN;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;


    private static final String[] WHITE_LIST_URL = {
            LOGIN,
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/webjars/**",
            "/configuration/**",
            BASE_URL + "/**"
    };


    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }



    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, ObjectMapper objectMapper, IUserPersistencePort userPersistencePort) throws Exception {

        return http.authorizeHttpRequests((authz) -> authz
                        .requestMatchers(WHITE_LIST_URL).permitAll()
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .exceptionHandling(ex -> ex.accessDeniedHandler(customAccessDeniedHandler)
                        .authenticationEntryPoint(customAuthenticationEntryPoint))
                .addFilter(new AuthenticationFilter(authenticationManager()))
                .addFilter(new ValidationFilter(authenticationManager(), objectMapper, userPersistencePort))
                .csrf(config -> config.disable())
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }
}

