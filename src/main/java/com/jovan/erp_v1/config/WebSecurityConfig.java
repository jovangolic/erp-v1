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
        
        private static final Map<String, Map<String, List<String>>> WRITE_ACCESS = Map.ofEntries(
        	    Map.entry("SUPERADMIN", Map.ofEntries(
        	        Map.entry("/**", ALL_HTTP_METHODS)
        	    )),
        	    Map.entry("ADMIN", Map.ofEntries(
        	        Map.entry("/**", ALL_HTTP_METHODS)
        	    )),
        	    Map.entry("ACCOUNTANT", Map.ofEntries(
        	        Map.entry("/accounts", BASIC_WRITE_METHODS),
        	        Map.entry("/balanceSheets", BASIC_WRITE_METHODS),
        	        Map.entry("/fiscalQuarters", BASIC_WRITE_METHODS),
        	        Map.entry("/fiscalYears", BASIC_WRITE_METHODS),
        	        Map.entry("/incomeStatements", BASIC_WRITE_METHODS),
        	        Map.entry("/invoices", BASIC_WRITE_METHODS),
        	        Map.entry("/journalEntries", BASIC_WRITE_METHODS),
        	        Map.entry("/journalItems", BASIC_WRITE_METHODS),
        	        Map.entry("/ledgerEntries", BASIC_WRITE_METHODS)
        	    )),
        	    Map.entry("AUDITOR", Map.ofEntries(
        	        Map.entry("/eventLogs", BASIC_WRITE_METHODS)
        	    )),
        	    Map.entry("SECURITY_AUDITOR", Map.ofEntries(
        	        Map.entry("/audit-logs", BASIC_WRITE_METHODS),
        	        Map.entry("/barCodes", BASIC_WRITE_METHODS),
        	        Map.entry("/eventLogs", BASIC_WRITE_METHODS)
        	    )),
        	    Map.entry("STORAGE_FOREMAN", Map.ofEntries(
        	        Map.entry("/barCodes", BASIC_WRITE_METHODS),
        	        Map.entry("/confirmationDocuments", BASIC_WRITE_METHODS),
        	        Map.entry("/inventoryItems", BASIC_WRITE_METHODS),
        	        Map.entry("/inboundDeliveries", BASIC_WRITE_METHODS),
        	        Map.entry("/outboundDeliveries", BASIC_WRITE_METHODS),
        	        Map.entry("/inventories", BASIC_WRITE_METHODS),
        	        Map.entry("/logistics-providers", BASIC_WRITE_METHODS)
        	    )),
        	    Map.entry("STORAGE_EMPLOYEE", Map.ofEntries(
        	        Map.entry("/barCodes", BASIC_WRITE_METHODS),
        	        Map.entry("/outboundDeliveries", BASIC_WRITE_METHODS),
        	        Map.entry("/inventoryItems", BASIC_WRITE_METHODS)
        	    )),
        	    Map.entry("INVENTORY_MANAGER", Map.ofEntries(
        	        Map.entry("/barCodes", BASIC_WRITE_METHODS),
        	        Map.entry("/inboundDeliveries", BASIC_WRITE_METHODS),
        	        Map.entry("/outboundDeliveries", BASIC_WRITE_METHODS),
        	        Map.entry("/inventories", BASIC_WRITE_METHODS),
        	        Map.entry("/inventoryItems", BASIC_WRITE_METHODS)
        	    )),
        	    Map.entry("INVENTORY_APPROVER", Map.ofEntries(
        	        Map.entry("/inventories", BASIC_WRITE_METHODS)
        	    )),
        	    Map.entry("FINANCIAL_MANAGER", Map.ofEntries(
        	        Map.entry("/fiscalQuarters", BASIC_WRITE_METHODS),
        	        Map.entry("/fiscalYears", BASIC_WRITE_METHODS),
        	        Map.entry("/incomeStatements", BASIC_WRITE_METHODS),
        	        Map.entry("/invoices", BASIC_WRITE_METHODS),
        	        Map.entry("/journalEntries", BASIC_WRITE_METHODS),
        	        Map.entry("/journalItems", BASIC_WRITE_METHODS),
        	        Map.entry("/ledgerEntries", BASIC_WRITE_METHODS)
        	    )),
        	    Map.entry("MANAGER", Map.ofEntries(
        	        Map.entry("/fiscalQuarters", BASIC_WRITE_METHODS),
        	        Map.entry("/fiscalYears", BASIC_WRITE_METHODS)
        	    )),
        	    Map.entry("PRODUCTION_PLANNER", Map.ofEntries(
        	        Map.entry("/billOfMaterials", BASIC_WRITE_METHODS),
        	        Map.entry("/capacityPlannings", BASIC_WRITE_METHODS),
        	        Map.entry("/inboundDeliveries", BASIC_WRITE_METHODS),
        	        Map.entry("/outboundDeliveries", BASIC_WRITE_METHODS)
        	    )),
        	    Map.entry("QUALITY_MANAGER", Map.ofEntries(
        	        Map.entry("/billOfMaterials", BASIC_WRITE_METHODS),
        	        Map.entry("/eventLogs", BASIC_WRITE_METHODS)
        	    )),
        	    Map.entry("PROCUREMENT", Map.ofEntries(
        	        Map.entry("/buyers", BASIC_WRITE_METHODS),
        	        Map.entry("/confirmationDocuments", BASIC_WRITE_METHODS),
        	        Map.entry("/delivery-items", BASIC_WRITE_METHODS),
        	        Map.entry("/drivers", BASIC_WRITE_METHODS),
        	        Map.entry("/inboundDeliveries", BASIC_WRITE_METHODS),
        	        Map.entry("/logistics-providers", BASIC_WRITE_METHODS)
        	    )),
        	    Map.entry("SALES", Map.ofEntries(
        	        Map.entry("/buyers", BASIC_WRITE_METHODS),
        	        Map.entry("/confirmationDocuments", BASIC_WRITE_METHODS),
        	        Map.entry("/delivery-items", BASIC_WRITE_METHODS),
        	        Map.entry("/drivers", BASIC_WRITE_METHODS),
        	        Map.entry("/outboundDeliveries", BASIC_WRITE_METHODS),
        	        Map.entry("/invoices", BASIC_WRITE_METHODS),
        	        Map.entry("/itemSales", BASIC_WRITE_METHODS)
        	    )),
        	    Map.entry("LOGISTICS_MANAGER", Map.ofEntries(
        	    	Map.entry("/logistics-providers", BASIC_WRITE_METHODS)))
        	);

        
        private static final Map<String, Map<String, List<String>>> READ_ACCESS = Map.ofEntries(
        	    Map.entry("AUDITOR", Map.ofEntries(
        	        Map.entry("/accounts", BASIC_READ_METHODS),
        	        Map.entry("/incomeStatements", BASIC_READ_METHODS),
        	        Map.entry("/audit-logs", BASIC_READ_METHODS),
        	        Map.entry("/balanceSheets", BASIC_READ_METHODS),
        	        Map.entry("/buyers", BASIC_READ_METHODS),
        	        Map.entry("/fiscalQuarters", BASIC_READ_METHODS),
        	        Map.entry("/fiscalYears", BASIC_READ_METHODS),
        	        Map.entry("/confirmationDocuments", BASIC_READ_METHODS),
        	        Map.entry("/delivery-items", BASIC_READ_METHODS),
        	        Map.entry("/invoices", BASIC_READ_METHODS),
        	        Map.entry("/itemSales", BASIC_READ_METHODS),
        	        Map.entry("/journalEntries", BASIC_READ_METHODS),
        	        Map.entry("/journalItems", BASIC_READ_METHODS),
        	        Map.entry("/ledgerEntries", BASIC_READ_METHODS),
        	        Map.entry("/logistics-providers", BASIC_READ_METHODS)
        	    )),
        	    Map.entry("FINANCIAL_MANAGER", Map.ofEntries(
        	        Map.entry("/accounts", BASIC_READ_METHODS),
        	        Map.entry("/inboundDeliveries", BASIC_READ_METHODS),
        	        Map.entry("/outboundDeliveries", BASIC_READ_METHODS),
        	        Map.entry("/balanceSheets", BASIC_READ_METHODS),
        	        Map.entry("/confirmationDocuments", BASIC_READ_METHODS),
        	        Map.entry("/delivery-items", BASIC_READ_METHODS),
        	        Map.entry("/eventLogs", BASIC_READ_METHODS),
        	        Map.entry("/itemSales", BASIC_READ_METHODS)
        	    )),
        	    Map.entry("ACCOUNTANT", Map.ofEntries(
        	        Map.entry("/confirmationDocuments", BASIC_READ_METHODS),
        	        Map.entry("/delivery-items", BASIC_READ_METHODS),
        	        Map.entry("/eventLogs", BASIC_READ_METHODS)
        	    )),
        	    Map.entry("PRODUCTION_PLANNER", Map.ofEntries(
        	        Map.entry("/barCodes", BASIC_READ_METHODS),
        	        Map.entry("/delivery-items", BASIC_READ_METHODS),
        	        Map.entry("/goods", BASIC_READ_METHODS)
        	    )),
        	    Map.entry("QUALITY_MANAGER", Map.ofEntries(
        	        Map.entry("/barCodes", BASIC_READ_METHODS),
        	        Map.entry("/goods", BASIC_READ_METHODS),
        	        Map.entry("/outboundDeliveries", BASIC_READ_METHODS),
        	        Map.entry("/capacityPlannings", BASIC_READ_METHODS),
        	        Map.entry("/inboundDeliveries", BASIC_READ_METHODS),
        	        Map.entry("/inventories", BASIC_READ_METHODS),
        	        Map.entry("/confirmationDocuments", BASIC_READ_METHODS),
        	        Map.entry("/delivery-items", BASIC_READ_METHODS),
        	        Map.entry("/inventoryItems", BASIC_READ_METHODS)
        	    )),
        	    Map.entry("INVENTORY_MANAGER", Map.ofEntries(
        	        Map.entry("/billOfMaterials", BASIC_READ_METHODS),
        	        Map.entry("/goods", BASIC_READ_METHODS),
        	        Map.entry("/buyers", BASIC_READ_METHODS),
        	        Map.entry("/capacityPlannings", BASIC_READ_METHODS),
        	        Map.entry("/eventLogs", BASIC_READ_METHODS),
        	        Map.entry("/confirmationDocuments", BASIC_READ_METHODS),
        	        Map.entry("/delivery-items", BASIC_READ_METHODS),
        	        Map.entry("/drivers", BASIC_READ_METHODS),
        	        Map.entry("/invoices", BASIC_READ_METHODS),
        	        Map.entry("/itemSales", BASIC_READ_METHODS),
        	        Map.entry("/logistics-providers", BASIC_READ_METHODS)
        	    )),
        	    Map.entry("SECURITY_AUDITOR", Map.ofEntries(
        	        Map.entry("/buyers", BASIC_READ_METHODS),
        	        Map.entry("/fiscalQuarters", BASIC_READ_METHODS),
        	        Map.entry("/inboundDeliveries", BASIC_READ_METHODS),
        	        Map.entry("/incomeStatements", BASIC_READ_METHODS),
        	        Map.entry("/capacityPlannings", BASIC_READ_METHODS),
        	        Map.entry("/fiscalYears", BASIC_READ_METHODS),
        	        Map.entry("/outboundDeliveries", BASIC_READ_METHODS),
        	        Map.entry("/confirmationDocuments", BASIC_READ_METHODS),
        	        Map.entry("/delivery-items", BASIC_READ_METHODS),
        	        Map.entry("/drivers", BASIC_READ_METHODS),
        	        Map.entry("/inventories", BASIC_READ_METHODS),
        	        Map.entry("/inventoryItems", BASIC_READ_METHODS),
        	        Map.entry("/invoices", BASIC_READ_METHODS),
        	        Map.entry("/itemSales", BASIC_READ_METHODS),
        	        Map.entry("/journalEntries", BASIC_READ_METHODS),
        	        Map.entry("/journalItems", BASIC_READ_METHODS),
        	        Map.entry("/ledgerEntries", BASIC_READ_METHODS),
        	        Map.entry("/logistics-providers", BASIC_READ_METHODS)
        	    )),
        	    Map.entry("STORAGE_FOREMAN", Map.ofEntries(
        	        Map.entry("/capacityPlannings", BASIC_READ_METHODS),
        	        Map.entry("/delivery-items", BASIC_READ_METHODS),
        	        Map.entry("/drivers", BASIC_READ_METHODS),
        	        Map.entry("/goods", BASIC_READ_METHODS)
        	    )),
        	    Map.entry("STORAGE_EMPLOYEE", Map.ofEntries(
        	        Map.entry("/capacityPlannings", BASIC_READ_METHODS),
        	        Map.entry("/confirmationDocuments", BASIC_READ_METHODS),
        	        Map.entry("/delivery-items", BASIC_READ_METHODS),
        	        Map.entry("/drivers", BASIC_READ_METHODS),
        	        Map.entry("/goods", BASIC_READ_METHODS)
        	    )),
        	    Map.entry("MANAGER", Map.ofEntries(
        	        Map.entry("/confirmationDocuments", BASIC_READ_METHODS),
        	        Map.entry("/delivery-items", BASIC_READ_METHODS),
        	        Map.entry("/eventLogs", BASIC_READ_METHODS)
        	    )),
        	    Map.entry("PROCUREMENT", Map.ofEntries(
        	        Map.entry("/goods", BASIC_READ_METHODS),
        	        Map.entry("/invoices", BASIC_READ_METHODS),
        	        Map.entry("/itemSales", BASIC_READ_METHODS)
        	    )),
        	    Map.entry("CUSTOMER_SERVICE", Map.ofEntries(
        	        Map.entry("/invoices", BASIC_READ_METHODS),
        	        Map.entry("/logistics-providers", BASIC_READ_METHODS)
        	    )),
        	    Map.entry("SALES", Map.ofEntries(
        	    	Map.entry("/logistics-providers", BASIC_READ_METHODS)))
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
