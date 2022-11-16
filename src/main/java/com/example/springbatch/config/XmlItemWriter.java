package com.example.springbatch.config;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.example.springbatch.model.Person;
@Component
public class XmlItemWriter implements ItemWriter<Person> {

	@Override
	public void write(List<? extends Person> items) throws Exception {
		items.forEach(person-> System.out.println(person.toString()));
	}

}
