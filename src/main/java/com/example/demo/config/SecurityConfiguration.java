package com.example.demo.config;

import com.example.demo.dto.ErrorDto;
import com.example.demo.service.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletOutputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import java.util.Date;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
        jsr250Enabled = true,
        securedEnabled = true
)
public class SecurityConfiguration {
    public final ObjectMapper mapper;
    public final CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(registry -> registry.anyRequest().permitAll())
                .exceptionHandling(errorConfig -> {
                    errorConfig.authenticationEntryPoint((request, response, authException) -> {
                        ErrorDto errorDto = new ErrorDto(request.getRequestURI(), 401,
                                new Date(), authException.getMessage());
                        ServletOutputStream outputStream = response.getOutputStream();
                        mapper.writeValue(outputStream, errorDto);
                        response.setStatus(401);
                        outputStream.flush();
                        outputStream.close();
                    });
                })
                .exceptionHandling(errorConfig -> {
                    errorConfig.accessDeniedHandler((request, response, accessDeniedException) -> {
                        ErrorDto errorDto = new ErrorDto(request.getRequestURI(), 403,
                                new Date(), accessDeniedException.getMessage());
                        ServletOutputStream outputStream = response.getOutputStream();
                        mapper.writeValue(outputStream, errorDto);
                        response.setStatus(403);
                        outputStream.flush();
                        outputStream.close();
                    });
                })
                .addFilterBefore(new JwtTokenFilter(customUserDetailsService), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedMethods(
                List.of("/**")
        );
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",configuration);
        return source;
    }
}
