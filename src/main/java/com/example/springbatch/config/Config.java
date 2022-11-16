package com.example.springbatch.config;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config extends DefaultBatchConfigurer {
	@Override
	public void setDataSource(DataSource dataSource) {
		// initialize will use a Map based JobRepository (instead of database)
	}
}
