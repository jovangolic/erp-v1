package com.jovan.erp_v1;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

import com.jovan.erp_v1.enumeration.SystemStatus;
import com.jovan.erp_v1.model.SystemState;
import com.jovan.erp_v1.repository.SystemStateRepository;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication(scanBasePackages = "com.jovan.erp_v1")
@EnableAsync
@EnableCaching
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class ErpV1Application {

	public static void main(String[] args) {
		// Učitaj .env fajl i postavi promenljive kao sistemske
		/*
		 * Dotenv dotenv = Dotenv.configure()
		 * .ignoreIfMissing() // ako .env ne postoji, neće pasti
		 * .load();
		 * 
		 * dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(),
		 * entry.getValue()));
		 */

		SpringApplication.run(ErpV1Application.class, args);
	}

	@Bean
	public CommandLineRunner initSystemState(SystemStateRepository repo) {
		return args -> {
			if (repo.count() == 0) {
				SystemState initial = new SystemState();
				initial.setMaintenanceMode(false);
				initial.setRegistrationEnabled(true);
				initial.setLastRestartTime(LocalDateTime.now());
				initial.setSystemVersion("1.0.0");
				initial.setStatusMessage(SystemStatus.RUNNING); // Enum se koristi ovde
				repo.save(initial);
			}
		};
	}

}
