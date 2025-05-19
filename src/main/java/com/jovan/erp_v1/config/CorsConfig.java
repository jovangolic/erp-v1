package com.jovan.erp_v1.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {

	private static final Long MAX_AGE = 3600L;
    private static final int CORS_FILTER_ORDER = -102;
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();      
        // Ako koristiš cookie/session/autentifikaciju iz browsera
        config.setAllowCredentials(true);
        // Dozvoljeni domeni za frontend (lokalno i eventualno deploy-ovani)
        config.setAllowedOrigins(List.of("http://localhost:5173")); // dodaj još domena ako imaš
        // Dozvoljeni HTTP headeri
        config.setAllowedHeaders(List.of(
            HttpHeaders.AUTHORIZATION,
            HttpHeaders.CONTENT_TYPE,
            HttpHeaders.ACCEPT
        ));
        // Ako backend vraća Authorization header (npr. JWT)
        config.setExposedHeaders(List.of(HttpHeaders.AUTHORIZATION));
        // Dozvoljene metode
        config.setAllowedMethods(List.of(
            HttpMethod.GET.name(),
            HttpMethod.POST.name(),
            HttpMethod.PUT.name(),
            HttpMethod.DELETE.name(),
            HttpMethod.OPTIONS.name()
        ));
        config.setMaxAge(3600L); // Cache trajanje CORS preflight odgovora (u sekundama)
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
