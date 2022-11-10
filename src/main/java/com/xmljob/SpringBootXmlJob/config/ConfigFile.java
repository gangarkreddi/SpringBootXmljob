package com.xmljob.SpringBootXmlJob.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.xstream.XStreamMarshaller;
import com.xmljob.SpringBootXmlJob.model.Person;

@Configuration
public class ConfigFile {
	@Autowired
	public JobBuilderFactory jobBuilderFactory;
	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	@Autowired
	private DataSource dataSource;

	@Bean
	public StaxEventItemReader<Person> xmlItemReader() {

		/*
		 * StaxEventItemReader<Person> reader = new
		 * StaxEventItemReaderBuilder<Person>().name("itemReader") .resource(new
		 * ClassPathResource("persons.xml")).addFragmentRootElements("person")
		 * .unmarshaller(personMarshaller()).build(); return reader ;
		 */
		StaxEventItemReader<Person> reader = new StaxEventItemReader<Person>();
		reader.setResource(new ClassPathResource("persons.xml"));
		reader.setFragmentRootElementName("person");
		
		Map<String,String> aliasesMap =new HashMap<String,String>();
		aliasesMap.put("person", "com.xmljob.SpringBootXmlJob.model.Person");
		XStreamMarshaller marshaller = new XStreamMarshaller();
		marshaller.setAliases(aliasesMap);
		
		reader.setUnmarshaller(marshaller);
		return reader;
	}

	private XStreamMarshaller personMarshaller() {
		Map<String, Class> aliases = new HashMap<>();
		aliases.put("person", Person.class);
		aliases.put("personId", Long.class);
		aliases.put("firstName", String.class);
		aliases.put("lastName", String.class);
		aliases.put("email", String.class);
		aliases.put("age", Integer.class);
		XStreamMarshaller marshaller = new XStreamMarshaller();
		marshaller.setAliases(aliases);
		return marshaller;
	}	
	
	 @Bean
	    public PersonItenProcessor xmlItemrocessor() {
	        return new PersonItenProcessor();
	    }

	@Bean
	public JdbcBatchItemWriter<Person> xmlItemWriter() {
		/*
		 * JdbcBatchItemWriter<Person> writer = new JdbcBatchItemWriter<>();
		 * writer.setDataSource(this.dataSource); // writer.
		 * setSql("insert into student(id,roll_number,name) values (:id,:rollNumber,:name)"
		 * ); writer.
		 * setSql("INSERT INTO ABC_TABLE(PERSON_ID,FIRST_NAME,LAST_NAME,EMAIL,AGE) VALUES (:personId,:firstName,:lastName ,:email ,:age)"
		 * ); writer.setItemSqlParameterSourceProvider(new
		 * BeanPropertyItemSqlParameterSourceProvider<>()); writer.afterPropertiesSet();
		 * return writer;
		 */
		JdbcBatchItemWriter<Person> writer = new JdbcBatchItemWriter<Person>();
		writer.setDataSource(dataSource);
		writer.setSql("INSERT INTO ABC_TABLE(person_id,first_name,last_name,email,age) VALUES(?,?,?,?,?)");
		writer.setItemPreparedStatementSetter(new PersonPreparedStatementSetter());
		return writer;
	}
	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1")
				.<Person, Person> chunk(1000)
				.reader(xmlItemReader())
				.processor(xmlItemrocessor())
				.writer(xmlItemWriter())
				.build();
	}
	
	@Bean
	public Job job() {
		return jobBuilderFactory.get("job")
				.start(step1())
				.build();
	}	
}
