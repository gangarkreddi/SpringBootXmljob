package com.xmljob.SpringBootXmlJob.controller;

import java.util.Set;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/batchjob")
public class BatchController {
	@Autowired
	private JobLauncher jobLauncher;
	@Autowired
	private Job job;
	@Autowired
	private DataSource dataSource;
	@Autowired
	JobOperator jobOperator ;
	
	@RequestMapping("/exectute")
	public void executeJobLauncher() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		JobParameters jobParameters = new JobParametersBuilder()
				.addString("JobName0", "XmlJob0")
                .toJobParameters();
		
		JobExecution jobExecution = jobLauncher.run(job, jobParameters);
		System.out.println("STATUS :: "+jobExecution.getStatus());		
		jobExecution.stop();
		
		System.out.println("STATUS :: "+jobExecution.getStatus());	
		String jobName = jobExecution.getJobInstance().getJobName(); 		
		JobExplorerFactoryBean factory = new JobExplorerFactoryBean();
		factory.setDataSource(dataSource);
		factory.setJdbcOperations(new JdbcTemplate(dataSource));
		JobExplorer jobExplorer;
		try {
			jobExplorer = factory.getObject();
			Set<JobExecution> jobExecutions = jobExplorer.findRunningJobExecutions(jobName);
			jobExecutions.forEach(job -> {
				try {
					jobOperator.stop(job.getId());
				} catch (NoSuchJobExecutionException | JobExecutionNotRunningException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
}
