package com.jovan.erp_v1.config;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Map;
import java.util.List;
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
        private final CustomAccessDeniedHandler accessDeniedHandler;

        private static final String[] STORAGE_EMPLOYEE_ENDPOINTS = {
                        "/vendor/**", "/goods/**", "/invoice/**", "/storage/**",
                        "/rawMaterial/**", "/confirmationDocument/**",
                        "/sales/**", "/supply/**", "/salesOrder/**",
                        "/inventory/**", "/inventoryItems/**", "/driver/**", "/vehicle/**", ",deliveryItem/**",
                        "/logisticsProvider/**", "inboundDelivery/**", "outboundDelivery/**",
                        "shipment/**", "stockTransfer/**", "stockTransferItem", "/transportOrder/**"
        };

        private static final String[] STORAGE_FOREMAN_ENDPOINTS = {
                        "/buyer/**", "/confirmationDocument/**", "/goods/**", "/invoice/**",
                        "/itemSales/**", "/payment/**", "/product/**", "/procurement/**",
                        "/rawMaterial/**", "/sales/**", "/salesOrder/**", "/storage/**",
                        "/supply/**", "/supplyitem/**", "/vendor/**", "/shift/**",
                        "/shiftReport/**", "/inventory/**", "/inventoryItems/**", "/shelf/**", "/driver/**",
                        "/vehicle/**",
                        ",deliveryItem/**", "/logisticsProvider/**", "inboundDelivery/**", "outboundDelivery/**",
                        "shipment/**", "stockTransfer/**", "stockTransferItem/**", "/transportOrder/**"
        };
        
        private static final List<String> ALL_HTTP_METHODS = List.of("GET:/**","POST:/**","PUT:/**","DELETE:/**","PATCH:/**","OPTIONS:/**","HEAD:/**","TRACE:/**");
        private static final List<String> BASIC_WRITE_METHODS = List.of("POST:/**","PUT:/**","DELETE:/**","GET:/**");
        private static final List<String> BASIC_READ_METHODS = List.of("GET:/**");
        
        private static final Map<String, Map<String, List<String>>> WRITE_ACCESS = Map.of(
        		"SUPERADMIN", Map.of("/**", ALL_HTTP_METHODS),
        	    "ADMIN", Map.of("/**", ALL_HTTP_METHODS),
        	    "ACCOUNTANT", Map.of("/accounts",BASIC_WRITE_METHODS),
        	    "SECURITY_AUDITOR",Map.of("/audit-logs",BASIC_WRITE_METHODS)
        );
        
        private static final Map<String, Map<String, List<String>>> READ_ACCESS = Map.of(
        	    "AUDITOR", Map.of(
        	        "/accounts", BASIC_READ_METHODS,
        	        "/audit-logs", BASIC_READ_METHODS
        	    ),
        	    "FINANCIAL_MANAGER", Map.of(
        	        "/accounts", BASIC_READ_METHODS
        	    )
        	);

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
                                .exceptionHandling(ex -> ex
                                                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                                                .accessDeniedHandler(accessDeniedHandler))
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(auth -> auth
      
                                                .requestMatchers("/auth/login", "/users/create-superadmin").permitAll()
                                                .requestMatchers("/auth/register").hasRole("ADMIN")

                                                .requestMatchers("/admin/**", "/users/create-admin/**")
                                                .hasRole("SUPERADMIN")
                                                .requestMatchers("/user/**", "/role/**", "/users/admin/**")
                                                .hasRole("ADMIN")
                                                .requestMatchers("/email/**").hasRole("ADMIN")
                                                .requestMatchers("/email/**").hasRole("SUPERADMIN")
                                                .requestMatchers("/companyEmail/**").hasRole("ADMIN")
                                                .requestMatchers("/email/**").hasRole("SUPERADMIN")
                                                .requestMatchers("/journalEntries/**", "/ledgerEntries/**",
                                                                "/incomeStatements/**",
                                                                "/fiscalYears/**", "/fiscalQuarters/**",
                                                                "/balanceSheets/**",
                                                                "/accounts/**", "/journalItems/**", "/taxRates/**")
                                                .hasAnyRole("ADMIN", "SUPERADMIN")
                                                .requestMatchers(STORAGE_EMPLOYEE_ENDPOINTS).hasRole("STORAGE_EMPLOYEE")
                                                .requestMatchers(STORAGE_FOREMAN_ENDPOINTS).hasRole("STORAGE_FOREMAN")
                                                
                                                .anyRequest().authenticated())
                                .logout(logout -> logout
                                                .logoutSuccessHandler(
                                                                (request, response, authentication) -> response
                                                                                .setStatus(HttpServletResponse.SC_OK)));

                http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
                return http.build();
        }
}
