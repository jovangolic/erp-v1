package com.jovan.erp_v1.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.jovan.erp_v1.security.JwtAuthenticationEntryPoint;
import com.jovan.erp_v1.security.JwtAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
@EnableWebSecurity
public class WebSecurityConfig implements WebMvcConfigurer {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    // Endpoint grupe za čitljivost i održavanje
    private static final String[] STORAGE_EMPLOYEE_ENDPOINTS = {
            "/vendor/**", "/goods/**", "/invoice/**", "/storage/**",
            "/rawMaterial/**", "/confirmationDocument/**",
            "/sales/**", "/supply/**", "/salesOrder/**",
            "/inventory/**", "/inventoryItems/**", "/driver/**", "/vehicle/**", ",deliveryItem/**",
            "/logisticsProvider/**", "inboundDelivery/**", "outboundDelivery/**",
            "shipment/**"
    };

    private static final String[] STORAGE_FOREMAN_ENDPOINTS = {
            "/buyer/**", "/confirmationDocument/**", "/goods/**", "/invoice/**",
            "/itemSales/**", "/payment/**", "/product/**", "/procurement/**",
            "/rawMaterial/**", "/sales/**", "/salesOrder/**", "/storage/**",
            "/supply/**", "/supplyitem/**", "/vendor/**", "/shift/**",
            "/shiftReport/**", "/inventory/**", "/inventoryItems/**", "/shelf/**", "/driver/**", "/vehicle/**",
            ",deliveryItem/**", "/logisticsProvider/**", "inboundDelivery/**", "outboundDelivery/**",
            "shipment/**"
    };

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToFileExtensionConverter());
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authenticationProvider(authenticationProvider())
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 🔓 Endpointi dostupni bez autentifikacije
                        .requestMatchers("/auth/login", "/users/create-superadmin").permitAll()
                        .requestMatchers("/auth/register").hasRole("ADMIN")

                        // 🔒 Pristup prema ulogama
                        .requestMatchers("/admin/**", "/users/create-admin/**").hasRole("SUPERADMIN")
                        .requestMatchers("/user/**", "/role/**", "/users/admin/**").hasRole("ADMIN")
                        .requestMatchers(STORAGE_EMPLOYEE_ENDPOINTS).hasRole("STORAGE_EMPLOYEE")
                        .requestMatchers(STORAGE_FOREMAN_ENDPOINTS).hasRole("STORAGE_FOREMAN")

                        // ❗ Bilo koji drugi zahtev zahteva autentifikaciju
                        .anyRequest().authenticated())
                .logout(logout -> logout
                        .logoutSuccessHandler(
                                (request, response, authentication) -> response.setStatus(HttpServletResponse.SC_OK)));
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
