package com.example.springbatch.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import com.example.springbatch.model.Person;

@Configuration
public class BatchConfig {
	@Autowired
	XmlItemWriter xmlItemWriter;
	@Autowired
	XmlItemProcessor XmlItemProcessor;

	@Bean
	public Job helloWorlJob(JobBuilderFactory jobBuilders, StepBuilderFactory stepBuilders) {
		return jobBuilders.get("xmlob2").start(xmlstep(stepBuilders)).build();
	}

	@Bean
	public Step xmlstep(StepBuilderFactory stepBuilders) {
		return stepBuilders.get("xmlstep").<Person, Person>chunk(10).reader(xmlItemReader())
				.processor(XmlItemProcessor).writer(xmlItemWriter).build();
	}

	@Bean
	public StaxEventItemReader<Person> xmlItemReader() {

		StaxEventItemReader<Person> reader = new StaxEventItemReader<Person>();
		reader.setResource(new ClassPathResource("persons.xml"));
		reader.setFragmentRootElementName("person");

		Map<String, String> aliasesMap = new HashMap<String, String>();
		aliasesMap.put("person", "com.example.springbatch.model.Person");
		XStreamMarshaller marshaller = new XStreamMarshaller();
		marshaller.setAliases(aliasesMap);

		reader.setUnmarshaller(marshaller);
		return reader;
	}
}
