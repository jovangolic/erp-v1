package com.jovan.erp_v1;

import com.jovan.erp_v1.ErpV1Application;
import org.springframework.boot.SpringApplication;

public class TestErpV1Application {

	public static void main(String[] args) {
		SpringApplication.from(ErpV1Application::main).with(TestcontainersConfiguration.class).run(args);
	}

}
