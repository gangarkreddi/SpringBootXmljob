package com.example.springbatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class SpringBatchreadXmlApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBatchreadXmlApplication.class, args);
	}

}
