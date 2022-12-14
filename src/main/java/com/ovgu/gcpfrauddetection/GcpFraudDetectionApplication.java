package com.ovgu.gcpfrauddetection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class GcpFraudDetectionApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(GcpFraudDetectionApplication.class, args);
	}
}
