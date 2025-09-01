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
        	        Map.entry("/ledgerEntries", BASIC_WRITE_METHODS),
        	        Map.entry("/payments", BASIC_WRITE_METHODS),
        	        Map.entry("/procurements", BASIC_WRITE_METHODS),
        	        Map.entry("/reports", BASIC_WRITE_METHODS),
        	        Map.entry("/sales", BASIC_WRITE_METHODS),
        	        Map.entry("/salesOrders", BASIC_WRITE_METHODS),
        	        Map.entry("/supplies", BASIC_WRITE_METHODS),
        	        Map.entry("/suppliesItems", BASIC_WRITE_METHODS),
        	        Map.entry("/taxRates", BASIC_WRITE_METHODS),
        	        Map.entry("/vendors", BASIC_WRITE_METHODS),
        	        Map.entry("/transactions", BASIC_WRITE_METHODS)
        	    )),
        	    Map.entry("AUDITOR", Map.ofEntries(
        	        Map.entry("/eventLogs", BASIC_WRITE_METHODS)
        	    )),
        	    Map.entry("SECURITY_AUDITOR", Map.ofEntries(
        	        Map.entry("/audit-logs", BASIC_WRITE_METHODS),
        	        Map.entry("/barCodes", BASIC_WRITE_METHODS),
        	        Map.entry("/eventLogs", BASIC_WRITE_METHODS),
        	        Map.entry("/taxRates", BASIC_WRITE_METHODS)
        	    )),
        	    Map.entry("STORAGE_FOREMAN", Map.ofEntries(
        	        Map.entry("/barCodes", BASIC_WRITE_METHODS),
        	        Map.entry("/confirmationDocuments", BASIC_WRITE_METHODS),
        	        Map.entry("/inventoryItems", BASIC_WRITE_METHODS),
        	        Map.entry("/inboundDeliveries", BASIC_WRITE_METHODS),
        	        Map.entry("/outboundDeliveries", BASIC_WRITE_METHODS),
        	        Map.entry("/inventories", BASIC_WRITE_METHODS),
        	        Map.entry("/logistics-providers", BASIC_WRITE_METHODS),
        	        Map.entry("/materialMovements", BASIC_WRITE_METHODS),
        	        Map.entry("/materialRequests", BASIC_WRITE_METHODS),
        	        Map.entry("/materialRequirements", BASIC_WRITE_METHODS),
        	        Map.entry("/materialTransactions", BASIC_WRITE_METHODS),
        	        Map.entry("/reports", BASIC_WRITE_METHODS),
        	        Map.entry("/shelves", BASIC_WRITE_METHODS),
        	        Map.entry("/shifts", BASIC_WRITE_METHODS),
        	        Map.entry("/shiftPlannings", BASIC_WRITE_METHODS),
        	        Map.entry("/shiftReports", BASIC_WRITE_METHODS),
        	        Map.entry("/shipments", BASIC_WRITE_METHODS),
        	        Map.entry("/storages", BASIC_WRITE_METHODS)
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
        	        Map.entry("/inventoryItems", BASIC_WRITE_METHODS),
        	        Map.entry("/materialRequests", BASIC_WRITE_METHODS),
        	        Map.entry("/materialRequirements", BASIC_WRITE_METHODS),
        	        Map.entry("/materialTransactions", BASIC_WRITE_METHODS),
        	        Map.entry("/procurements", BASIC_WRITE_METHODS),
        	        Map.entry("/products", BASIC_WRITE_METHODS),
        	        Map.entry("/productionOrders", BASIC_WRITE_METHODS),
        	        Map.entry("/rawMaterials", BASIC_WRITE_METHODS),
        	        Map.entry("/shelves", BASIC_WRITE_METHODS),
        	        Map.entry("/stockTransfers", BASIC_WRITE_METHODS),
        	        Map.entry("/stockTransferItems", BASIC_WRITE_METHODS),
        	        Map.entry("/storages", BASIC_WRITE_METHODS),
        	        Map.entry("/defects", BASIC_WRITE_METHODS),
        	        Map.entry("/batches", BASIC_WRITE_METHODS)
        	    )),
        	    Map.entry("INVENTORY_APPROVER", Map.ofEntries(
        	        Map.entry("/inventories", BASIC_WRITE_METHODS),
        	        Map.entry("/shelves", BASIC_WRITE_METHODS)
        	    )),
        	    Map.entry("FINANCIAL_MANAGER", Map.ofEntries(
        	        Map.entry("/fiscalQuarters", BASIC_WRITE_METHODS),
        	        Map.entry("/fiscalYears", BASIC_WRITE_METHODS),
        	        Map.entry("/incomeStatements", BASIC_WRITE_METHODS),
        	        Map.entry("/invoices", BASIC_WRITE_METHODS),
        	        Map.entry("/journalEntries", BASIC_WRITE_METHODS),
        	        Map.entry("/journalItems", BASIC_WRITE_METHODS),
        	        Map.entry("/ledgerEntries", BASIC_WRITE_METHODS),
        	        Map.entry("/sales", BASIC_WRITE_METHODS),
        	        Map.entry("/salesOrders", BASIC_WRITE_METHODS),
        	        Map.entry("/transactions", BASIC_WRITE_METHODS)
        	    )),
        	    Map.entry("MANAGER", Map.ofEntries(
        	        Map.entry("/fiscalQuarters", BASIC_WRITE_METHODS),
        	        Map.entry("/fiscalYears", BASIC_WRITE_METHODS)
        	    )),
        	    Map.entry("PRODUCTION_PLANNER", Map.ofEntries(
        	        Map.entry("/billOfMaterials", BASIC_WRITE_METHODS),
        	        Map.entry("/capacityPlannings", BASIC_WRITE_METHODS),
        	        Map.entry("/inboundDeliveries", BASIC_WRITE_METHODS),
        	        Map.entry("/outboundDeliveries", BASIC_WRITE_METHODS),
        	        Map.entry("/materials", BASIC_WRITE_METHODS),
        	        Map.entry("/materialMovements", BASIC_WRITE_METHODS),
        	        Map.entry("/materialRequests", BASIC_WRITE_METHODS),
        	        Map.entry("/materialRequirements", BASIC_WRITE_METHODS),
        	        Map.entry("/materialTransactions", BASIC_WRITE_METHODS),
        	        Map.entry("/products", BASIC_WRITE_METHODS),
        	        Map.entry("/productionOrders", BASIC_WRITE_METHODS),
        	        Map.entry("/rawMaterials", BASIC_WRITE_METHODS),
        	        Map.entry("/reports", BASIC_WRITE_METHODS),
        	        Map.entry("/stockTransfers", BASIC_WRITE_METHODS),
        	        Map.entry("/stockTransferItems", BASIC_WRITE_METHODS),
        	        Map.entry("/workCenters", BASIC_WRITE_METHODS),
        	        Map.entry("/batches", BASIC_WRITE_METHODS)
        	    )),
        	    Map.entry("QUALITY_MANAGER", Map.ofEntries(
        	        Map.entry("/billOfMaterials", BASIC_WRITE_METHODS),
        	        Map.entry("/eventLogs", BASIC_WRITE_METHODS),
        	        Map.entry("/products", BASIC_WRITE_METHODS),
        	        Map.entry("/rawMaterials", BASIC_WRITE_METHODS),
        	        Map.entry("/reports", BASIC_WRITE_METHODS),
        	        Map.entry("/defects", BASIC_WRITE_METHODS),
        	        Map.entry("/batches", BASIC_WRITE_METHODS),
        	        Map.entry("/qualityChecks", BASIC_WRITE_METHODS),
        	        Map.entry("/testMeasurements", BASIC_WRITE_METHODS),
        	        Map.entry("/qualityStandards", BASIC_WRITE_METHODS),
        	        Map.entry("/inspections", BASIC_WRITE_METHODS),
        	        Map.entry("/inspectionDefects", BASIC_WRITE_METHODS)
        	    )),
        	    Map.entry("PROCUREMENT", Map.ofEntries(
        	        Map.entry("/buyers", BASIC_WRITE_METHODS),
        	        Map.entry("/confirmationDocuments", BASIC_WRITE_METHODS),
        	        Map.entry("/delivery-items", BASIC_WRITE_METHODS),
        	        Map.entry("/drivers", BASIC_WRITE_METHODS),
        	        Map.entry("/inboundDeliveries", BASIC_WRITE_METHODS),
        	        Map.entry("/logistics-providers", BASIC_WRITE_METHODS),
        	        Map.entry("/materials", BASIC_WRITE_METHODS),
        	        Map.entry("/materialMovements", BASIC_WRITE_METHODS),
        	        Map.entry("/materialRequests", BASIC_WRITE_METHODS),
        	        Map.entry("/materialRequirements", BASIC_WRITE_METHODS),
        	        Map.entry("/materialTransactions", BASIC_WRITE_METHODS),
        	        Map.entry("/procurements", BASIC_WRITE_METHODS),
        	        Map.entry("/products", BASIC_WRITE_METHODS),
        	        Map.entry("/productionOrders", BASIC_WRITE_METHODS),
        	        Map.entry("/rawMaterials", BASIC_WRITE_METHODS),
        	        Map.entry("/reports", BASIC_WRITE_METHODS),
        	        Map.entry("/route", BASIC_WRITE_METHODS),
        	        Map.entry("/salesOrders", BASIC_WRITE_METHODS),
        	        Map.entry("/supplies", BASIC_WRITE_METHODS),
        	        Map.entry("/suppliesItems", BASIC_WRITE_METHODS),
        	        Map.entry("/vendors", BASIC_WRITE_METHODS)
        	    )),
        	    Map.entry("SALES", Map.ofEntries(
        	        Map.entry("/buyers", BASIC_WRITE_METHODS),
        	        Map.entry("/confirmationDocuments", BASIC_WRITE_METHODS),
        	        Map.entry("/delivery-items", BASIC_WRITE_METHODS),
        	        Map.entry("/drivers", BASIC_WRITE_METHODS),
        	        Map.entry("/outboundDeliveries", BASIC_WRITE_METHODS),
        	        Map.entry("/invoices", BASIC_WRITE_METHODS),
        	        Map.entry("/itemSales", BASIC_WRITE_METHODS),
        	        Map.entry("/payments", BASIC_WRITE_METHODS),
        	        Map.entry("/procurements", BASIC_WRITE_METHODS),
        	        Map.entry("/products", BASIC_WRITE_METHODS),
        	        Map.entry("/sales", BASIC_WRITE_METHODS),
        	        Map.entry("/salesOrders", BASIC_WRITE_METHODS)
        	    )),
        	    Map.entry("LOGISTICS_MANAGER", Map.ofEntries(
        	    	Map.entry("/logistics-providers", BASIC_WRITE_METHODS),
        	    	Map.entry("/materialRequests", BASIC_WRITE_METHODS),
        	    	Map.entry("/materialRequirements", BASIC_WRITE_METHODS),
        	    	Map.entry("/productionOrders", BASIC_WRITE_METHODS),
        	    	Map.entry("/reports", BASIC_WRITE_METHODS),
        	    	Map.entry("/route", BASIC_WRITE_METHODS),
        	    	Map.entry("/salesOrders", BASIC_WRITE_METHODS),
        	    	Map.entry("/shelves", BASIC_WRITE_METHODS),
        	    	Map.entry("/shipments", BASIC_WRITE_METHODS),
        	    	Map.entry("/stockTransfers", BASIC_WRITE_METHODS),
        	    	Map.entry("/stockTransferItems", BASIC_WRITE_METHODS),
        	    	Map.entry("/transportOrders", BASIC_WRITE_METHODS),
        	    	Map.entry("/trackingInfos", BASIC_WRITE_METHODS),
        	    	Map.entry("/vehicles", BASIC_WRITE_METHODS)
        	    	)),
        	    Map.entry("TRANSPORT_PLANNER", Map.ofEntries(
        	    	Map.entry("/route", BASIC_WRITE_METHODS),
        	    	Map.entry("/stockTransfers", BASIC_WRITE_METHODS),
        	    	Map.entry("/shipments", BASIC_WRITE_METHODS),
        	    	Map.entry("/stockTransferItems", BASIC_WRITE_METHODS),
        	    	Map.entry("/trackingInfos", BASIC_WRITE_METHODS),
        	    	Map.entry("/transportOrders", BASIC_WRITE_METHODS),
        	    	Map.entry("/vehicles", BASIC_WRITE_METHODS))),
        	    Map.entry("DISPATCHER", Map.ofEntries(
        	    	Map.entry("/route", BASIC_WRITE_METHODS),
        	    	Map.entry("/transportOrders", BASIC_WRITE_METHODS))),
        	    Map.entry("SALES_MANAGER", Map.ofEntries(
        	    	Map.entry("/sales", BASIC_WRITE_METHODS),
        	    	Map.entry("/salesOrders", BASIC_WRITE_METHODS))),
        	    Map.entry("HR_MANAGER", Map.ofEntries(
        	    	Map.entry("/shifts", BASIC_WRITE_METHODS),
        	    	Map.entry("/shiftPlannings", BASIC_WRITE_METHODS),
        	    	Map.entry("/shiftReports", BASIC_WRITE_METHODS))),
        	    Map.entry("DRIVER", Map.ofEntries(
                	Map.entry("/transportOrders", BASIC_WRITE_METHODS))),
        	    Map.entry("MAINTENANCE_MANAGER", Map.ofEntries(
        	    	Map.entry("/workCenters", BASIC_WRITE_METHODS),
        	    	Map.entry("/defects", BASIC_WRITE_METHODS),
        	    	Map.entry("/qualityChecks", BASIC_WRITE_METHODS),
        	    	Map.entry("/testMeasurements", BASIC_WRITE_METHODS))),
        	    Map.entry("PRODUCTION_MANAGER", Map.ofEntries(
            	    Map.entry("/workCenters", BASIC_WRITE_METHODS),
            	    Map.entry("/batches", BASIC_WRITE_METHODS),
            	    Map.entry("/qualityChecks", BASIC_WRITE_METHODS))),
        	    Map.entry("QUALITY_INSPECTOR", Map.ofEntries(
        	    	Map.entry("/qualityChecks", BASIC_WRITE_METHODS),
        	    	Map.entry("/testMeasurements", BASIC_WRITE_METHODS),
        	    	Map.entry("/inspections", BASIC_WRITE_METHODS),
        	    	Map.entry("/inspectionDefects", BASIC_WRITE_METHODS))),
        	    Map.entry("QUALITY_TECHNICIAN", Map.ofEntries(
        	    	Map.entry("/testMeasurements", BASIC_WRITE_METHODS),
        	    	Map.entry("/inspections", BASIC_WRITE_METHODS),
        	    	Map.entry("/inspectionDefects", BASIC_WRITE_METHODS)))
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
        	        Map.entry("/logistics-providers", BASIC_READ_METHODS),
        	        Map.entry("/payments", BASIC_READ_METHODS),
        	        Map.entry("/procurements", BASIC_READ_METHODS),
        	        Map.entry("/reports", BASIC_READ_METHODS),
        	        Map.entry("/route", BASIC_READ_METHODS),
        	        Map.entry("/sales", BASIC_READ_METHODS),
        	        Map.entry("/salesOrders", BASIC_READ_METHODS),
        	        Map.entry("/shelves", BASIC_READ_METHODS),
        	        Map.entry("/shifts", BASIC_READ_METHODS),
        	        Map.entry("/shiftPlannings", BASIC_READ_METHODS),
        	        Map.entry("/shiftReports", BASIC_READ_METHODS),
        	        Map.entry("/shipments", BASIC_READ_METHODS),
        	        Map.entry("/stockTransfers", BASIC_READ_METHODS),
        	        Map.entry("/stockTransferItems", BASIC_READ_METHODS),
        	        Map.entry("/storages", BASIC_READ_METHODS),
        	        Map.entry("/supplies", BASIC_READ_METHODS),
        	        Map.entry("/suppliesItems", BASIC_READ_METHODS),
        	        Map.entry("/taxRates", BASIC_READ_METHODS),
        	        Map.entry("/vendors", BASIC_READ_METHODS),
        	        Map.entry("/workCenters", BASIC_READ_METHODS),
        	        Map.entry("/transactions", BASIC_READ_METHODS)
        	    )),
        	    Map.entry("FINANCIAL_MANAGER", Map.ofEntries(
        	        Map.entry("/accounts", BASIC_READ_METHODS),
        	        Map.entry("/inboundDeliveries", BASIC_READ_METHODS),
        	        Map.entry("/outboundDeliveries", BASIC_READ_METHODS),
        	        Map.entry("/balanceSheets", BASIC_READ_METHODS),
        	        Map.entry("/confirmationDocuments", BASIC_READ_METHODS),
        	        Map.entry("/delivery-items", BASIC_READ_METHODS),
        	        Map.entry("/eventLogs", BASIC_READ_METHODS),
        	        Map.entry("/itemSales", BASIC_READ_METHODS),
        	        Map.entry("/payments", BASIC_READ_METHODS),
        	        Map.entry("/procurements", BASIC_READ_METHODS),
        	        Map.entry("/productionOrders", BASIC_READ_METHODS),
        	        Map.entry("/reports", BASIC_READ_METHODS),
        	        Map.entry("/taxRates", BASIC_READ_METHODS)
        	    )),
        	    Map.entry("ACCOUNTANT", Map.ofEntries(
        	        Map.entry("/confirmationDocuments", BASIC_READ_METHODS),
        	        Map.entry("/delivery-items", BASIC_READ_METHODS),
        	        Map.entry("/eventLogs", BASIC_READ_METHODS),
        	        Map.entry("/products", BASIC_READ_METHODS),
        	        Map.entry("/productionOrders", BASIC_READ_METHODS),
        	        Map.entry("/rawMaterials", BASIC_READ_METHODS),
        	        Map.entry("/route", BASIC_READ_METHODS),
        	        Map.entry("/stockTransfers", BASIC_READ_METHODS),
        	        Map.entry("/stockTransferItems", BASIC_READ_METHODS),
        	        Map.entry("/supplies", BASIC_READ_METHODS)
        	    )),
        	    Map.entry("PRODUCTION_PLANNER", Map.ofEntries(
        	        Map.entry("/barCodes", BASIC_READ_METHODS),
        	        Map.entry("/delivery-items", BASIC_READ_METHODS),
        	        Map.entry("/goods", BASIC_READ_METHODS),
        	        Map.entry("/shelves", BASIC_READ_METHODS),
        	        Map.entry("/shifts", BASIC_READ_METHODS),
        	        Map.entry("/shiftPlannings", BASIC_READ_METHODS),
        	        Map.entry("/storages", BASIC_READ_METHODS),
        	        Map.entry("/defects", BASIC_READ_METHODS),
        	        Map.entry("/qualityChecks", BASIC_READ_METHODS),
        	        Map.entry("/testMeasurements", BASIC_READ_METHODS),
        	        Map.entry("/qualityStandards", BASIC_READ_METHODS),
        	        Map.entry("/inspections", BASIC_READ_METHODS),
        	        Map.entry("/inspectionDefects", BASIC_READ_METHODS)
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
        	        Map.entry("/inventoryItems", BASIC_READ_METHODS),
        	        Map.entry("/materials", BASIC_READ_METHODS),
        	        Map.entry("/materialMovements", BASIC_READ_METHODS),
        	        Map.entry("/materialRequests", BASIC_READ_METHODS),
        	        Map.entry("/materialRequirements", BASIC_READ_METHODS),
        	        Map.entry("/materialTransactions", BASIC_READ_METHODS),
        	        Map.entry("/salesOrders", BASIC_READ_METHODS),
        	        Map.entry("/shelves", BASIC_READ_METHODS),
        	        Map.entry("/shiftReports", BASIC_READ_METHODS),
        	        Map.entry("/stockTransfers", BASIC_READ_METHODS),
        	        Map.entry("/stockTransferItems", BASIC_READ_METHODS),
        	        Map.entry("/storages", BASIC_READ_METHODS),
        	        Map.entry("/supplies", BASIC_READ_METHODS),
        	        Map.entry("/workCenters", BASIC_READ_METHODS)
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
        	        Map.entry("/logistics-providers", BASIC_READ_METHODS),
        	        Map.entry("/materials", BASIC_READ_METHODS),
        	        Map.entry("/materialMovements", BASIC_READ_METHODS),
        	        Map.entry("/reports", BASIC_READ_METHODS),
        	        Map.entry("/route", BASIC_READ_METHODS),
        	        Map.entry("/sales", BASIC_READ_METHODS),
        	        Map.entry("/supplies", BASIC_READ_METHODS),
        	        Map.entry("/suppliesItems", BASIC_READ_METHODS),
        	        Map.entry("/qualityChecks", BASIC_READ_METHODS),
        	        Map.entry("/testMeasurements", BASIC_READ_METHODS),
        	        Map.entry("/qualityStandards", BASIC_READ_METHODS),
        	        Map.entry("/inspections", BASIC_READ_METHODS),
        	        Map.entry("/inspectionDefects", BASIC_READ_METHODS)
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
        	        Map.entry("/logistics-providers", BASIC_READ_METHODS),
        	        Map.entry("/payments", BASIC_READ_METHODS),
        	        Map.entry("/procurements", BASIC_READ_METHODS),
        	        Map.entry("/reports", BASIC_READ_METHODS),
        	        Map.entry("/route", BASIC_READ_METHODS),
        	        Map.entry("/sales", BASIC_READ_METHODS),
        	        Map.entry("/salesOrders", BASIC_READ_METHODS),
        	        Map.entry("/stockTransfers", BASIC_READ_METHODS),
        	        Map.entry("/stockTransferItems", BASIC_READ_METHODS),
        	        Map.entry("/supplies", BASIC_READ_METHODS),
        	        Map.entry("/suppliesItems", BASIC_READ_METHODS),
        	        Map.entry("/vendors", BASIC_READ_METHODS),
        	        Map.entry("/workCenters", BASIC_READ_METHODS),
        	        Map.entry("/defects", BASIC_READ_METHODS),
        	        Map.entry("/inspectionDefects", BASIC_READ_METHODS),
        	        Map.entry("/transactions", BASIC_READ_METHODS)
        	    )),
        	    Map.entry("STORAGE_FOREMAN", Map.ofEntries(
        	        Map.entry("/capacityPlannings", BASIC_READ_METHODS),
        	        Map.entry("/delivery-items", BASIC_READ_METHODS),
        	        Map.entry("/drivers", BASIC_READ_METHODS),
        	        Map.entry("/goods", BASIC_READ_METHODS),
        	        Map.entry("/materials", BASIC_READ_METHODS),
        	        Map.entry("/procurements", BASIC_READ_METHODS),
        	        Map.entry("/products", BASIC_READ_METHODS),
        	        Map.entry("/productionOrders", BASIC_READ_METHODS),
        	        Map.entry("/rawMaterials", BASIC_READ_METHODS),
        	        Map.entry("/route", BASIC_READ_METHODS),
        	        Map.entry("/sales", BASIC_READ_METHODS),
        	        Map.entry("/salesOrders", BASIC_READ_METHODS),
        	        Map.entry("/stockTransfers", BASIC_READ_METHODS),
        	        Map.entry("/stockTransferItems", BASIC_READ_METHODS),
        	        Map.entry("/supplies", BASIC_READ_METHODS)
        	    )),
        	    Map.entry("STORAGE_EMPLOYEE", Map.ofEntries(
        	        Map.entry("/capacityPlannings", BASIC_READ_METHODS),
        	        Map.entry("/confirmationDocuments", BASIC_READ_METHODS),
        	        Map.entry("/delivery-items", BASIC_READ_METHODS),
        	        Map.entry("/drivers", BASIC_READ_METHODS),
        	        Map.entry("/goods", BASIC_READ_METHODS),
        	        Map.entry("/materials", BASIC_READ_METHODS),
        	        Map.entry("/materialMovements", BASIC_READ_METHODS),
        	        Map.entry("/materialRequests", BASIC_READ_METHODS),
        	        Map.entry("/materialRequirements", BASIC_READ_METHODS),
        	        Map.entry("/materialTransactions", BASIC_READ_METHODS),
        	        Map.entry("/products", BASIC_READ_METHODS),
        	        Map.entry("/productionOrders", BASIC_READ_METHODS),
        	        Map.entry("/rawMaterials", BASIC_READ_METHODS),
        	        Map.entry("/reports", BASIC_READ_METHODS),
        	        Map.entry("/route", BASIC_READ_METHODS),
        	        Map.entry("/stockTransfers", BASIC_READ_METHODS),
        	        Map.entry("/stockTransferItems", BASIC_READ_METHODS)
        	    )),
        	    Map.entry("MANAGER", Map.ofEntries(
        	        Map.entry("/confirmationDocuments", BASIC_READ_METHODS),
        	        Map.entry("/delivery-items", BASIC_READ_METHODS),
        	        Map.entry("/eventLogs", BASIC_READ_METHODS)
        	    )),
        	    Map.entry("PROCUREMENT", Map.ofEntries(
        	        Map.entry("/goods", BASIC_READ_METHODS),
        	        Map.entry("/invoices", BASIC_READ_METHODS),
        	        Map.entry("/itemSales", BASIC_READ_METHODS),
        	        Map.entry("/payments", BASIC_READ_METHODS),
        	        Map.entry("/sales", BASIC_READ_METHODS),
        	        Map.entry("/stockTransfers", BASIC_READ_METHODS),
        	        Map.entry("/stockTransferItems", BASIC_READ_METHODS)
        	    )),
        	    Map.entry("CUSTOMER_SERVICE", Map.ofEntries(
        	        Map.entry("/invoices", BASIC_READ_METHODS),
        	        Map.entry("/logistics-providers", BASIC_READ_METHODS),
        	        Map.entry("/payments", BASIC_READ_METHODS),
        	        Map.entry("/shipments", BASIC_READ_METHODS),
        	        Map.entry("/batches", BASIC_READ_METHODS),
        	        Map.entry("/qualityChecks", BASIC_READ_METHODS)
        	    )),
        	    Map.entry("SALES", Map.ofEntries(
        	    	Map.entry("/logistics-providers", BASIC_READ_METHODS),
        	    	Map.entry("/rawMaterials", BASIC_READ_METHODS),
        	    	Map.entry("/reports", BASIC_READ_METHODS),
        	    	Map.entry("/route", BASIC_READ_METHODS))),
        	    Map.entry("LOGISTICS_MANAGER", Map.ofEntries(
        	    	Map.entry("/route", BASIC_READ_METHODS))),
        	    Map.entry("DISPATCHER", Map.ofEntries(
            	    Map.entry("/routes", BASIC_READ_METHODS),
            	    Map.entry("/shipments", BASIC_READ_METHODS),
            	    Map.entry("/trackingInfos", BASIC_READ_METHODS),
            	    Map.entry("/vehicles", BASIC_READ_METHODS))),
            	Map.entry("SALES_MANAGER", Map.ofEntries(
            		Map.entry("/salesOrders", BASIC_READ_METHODS),
            		Map.entry("/batches", BASIC_READ_METHODS),
            		Map.entry("/qualityChecks", BASIC_READ_METHODS),
            		Map.entry("/testMeasurements", BASIC_READ_METHODS),
            		Map.entry("/qualityStandards", BASIC_READ_METHODS),
            		Map.entry("/inspections", BASIC_READ_METHODS),
            		Map.entry("/inspectionDefects", BASIC_READ_METHODS))),
            	Map.entry("HR_MANAGER", Map.ofEntries(
            	    Map.entry("/shifts", BASIC_READ_METHODS))),
            	Map.entry("DRIVER", Map.ofEntries(
                	Map.entry("/transportOrders", BASIC_READ_METHODS),
                	Map.entry("/vehicles", BASIC_READ_METHODS))),
            	Map.entry("MECHANIC", Map.ofEntries(
            		Map.entry("/vehicles", BASIC_READ_METHODS))),
            	Map.entry("PRODUCTION_MANAGER", Map.ofEntries(
                	Map.entry("/defects", BASIC_READ_METHODS),
                	Map.entry("/testMeasurements", BASIC_READ_METHODS),
                	Map.entry("/qualityStandards", BASIC_READ_METHODS),
                	Map.entry("/inspections", BASIC_READ_METHODS),
                	Map.entry("/inspectionDefects", BASIC_READ_METHODS))),
            	Map.entry("PRODUCTION_OPERATOR", Map.ofEntries(
            		Map.entry("/batches", BASIC_READ_METHODS))),
            	Map.entry("REGULATORY_AUDITOR", Map.ofEntries(
            		Map.entry("/batches", BASIC_READ_METHODS),
            		Map.entry("/qualityChecks", BASIC_READ_METHODS),
            		Map.entry("/testMeasurements", BASIC_READ_METHODS),
            		Map.entry("/qualityStandards", BASIC_READ_METHODS),
            		Map.entry("/inspections", BASIC_READ_METHODS),
            		Map.entry("/inspectionDefects", BASIC_READ_METHODS))),
            	Map.entry("DISPOSAL_MANAGER", Map.ofEntries(
            		Map.entry("/batches", BASIC_READ_METHODS))),
            	Map.entry("MAINTENANCE_MANAGER", Map.ofEntries(
            		Map.entry("/batches", BASIC_READ_METHODS),
            		Map.entry("/qualityStandards", BASIC_READ_METHODS),
            		Map.entry("/inspections", BASIC_READ_METHODS),
            		Map.entry("/inspectionDefects", BASIC_READ_METHODS))),
            	Map.entry("QUALITY_INSPECTOR", Map.ofEntries(
            		Map.entry("/qualityStandards", BASIC_READ_METHODS))),
            	Map.entry("QUALITY_TECHNICIAN", Map.ofEntries(
                	Map.entry("/qualityStandards", BASIC_READ_METHODS))),
            	Map.entry("SUPPLY_CHAIN_MANAGER", Map.ofEntries(
            		Map.entry("/inspectionDefects", BASIC_READ_METHODS)))
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
                                .authorizeHttpRequests(auth -> {
                                                auth.requestMatchers("/auth/login", "/users/create-superadmin").permitAll();
                                                auth.requestMatchers("/auth/register").hasRole("ADMIN");

                                                auth.requestMatchers("/admin/**", "/users/create-admin/**","/companyEmail/**","/email/**")
                                                .hasRole("SUPERADMIN");
                                                auth.requestMatchers("/user/**", "/role/**", "/users/admin/**","/email/**").hasRole("ADMIN");
                                                
                                                WRITE_ACCESS.forEach((role, endpoints) -> {
                                                    endpoints.forEach((pattern, methods) -> {
                                                        methods.forEach(method -> {
                                                            String[] split = method.split(":");
                                                            String httpMethod = split[0];
                                                            String path = split[1];
                                                            auth.requestMatchers(HttpMethod.valueOf(httpMethod), path)
                                                                .hasRole(role);
                                                        });
                                                    });
                                                });

                                                READ_ACCESS.forEach((role, endpoints) -> {
                                                    endpoints.forEach((pattern, methods) -> {
                                                        methods.forEach(method -> {
                                                            String[] split = method.split(":");
                                                            String httpMethod = split[0];
                                                            String path = split[1];
                                                            auth.requestMatchers(HttpMethod.valueOf(httpMethod), path)
                                                                .hasRole(role);
                                                        });
                                                    });
                                                });
                                                auth.anyRequest().authenticated();
                                })
                                .logout(logout -> logout
                                                .logoutSuccessHandler(
                                                                (request, response, authentication) -> response
                                                                                .setStatus(HttpServletResponse.SC_OK)));
                http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
                return http.build();
        }
}
