package com.jovan.erp_v1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication(scanBasePackages = "com.jovan.erp_v1")
@EnableAsync
@EnableCaching
public class ErpV1Application {

	public static void main(String[] args) {
		// Učitaj .env fajl i postavi promenljive kao sistemske
		Dotenv dotenv = Dotenv.configure()
				.ignoreIfMissing() // ako .env ne postoji, neće pasti
				.load();

		dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

		SpringApplication.run(ErpV1Application.class, args);
	}

}
