package com.xmljob.SpringBootXmlJob.config;

import org.springframework.batch.item.ItemProcessor;

import com.xmljob.SpringBootXmlJob.model.Person;

public class PersonItenProcessor implements ItemProcessor<Person, Person>{

	@Override
	public Person process(Person person) throws Exception {
		return person;
	}
}