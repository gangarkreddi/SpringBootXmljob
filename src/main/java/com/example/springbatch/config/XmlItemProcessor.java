package com.example.springbatch.config;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.example.springbatch.model.Person;

@Component
public class XmlItemProcessor implements ItemProcessor<Person, Person>{

	@Override
	public Person process(Person person) throws Exception {		
		return person;
	}
}