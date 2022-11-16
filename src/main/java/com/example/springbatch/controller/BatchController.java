package com.example.springbatch.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/batchjob")
@EnableBatchProcessing
public class BatchController {
	@Autowired
	private JobLauncher jobLauncher;
	@Autowired
	private Job helloWorlJob;

	
	@RequestMapping("/exectute")
	public void executeJobLauncher() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		JobParameters jobParameters = new JobParametersBuilder()
				.addString("JobName", "XmlJob0")
                .toJobParameters();
		JobExecution jobExecution = jobLauncher.run(helloWorlJob, jobParameters);
		JobInstance jobInstance = jobExecution.getJobInstance();
		System.out.println("STATUS :: "+jobInstance.getJobName());
		System.out.println("STATUS :: "+jobExecution.getStatus());		
		jobExecution.stop();
	}
}
