package com.jovan.erp_v1;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import io.github.cdimascio.dotenv.Dotenv;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class ErpV1ApplicationTests {

	@Test
	void contextLoads() {
	}

	@BeforeAll
	public static void loadEnv() {
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
		dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
	}

}
