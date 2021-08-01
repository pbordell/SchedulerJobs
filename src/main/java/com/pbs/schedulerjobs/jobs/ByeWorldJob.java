package com.pbs.schedulerjobs.jobs;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.pbs.schedulerjobs.service.JobsService;

@DisallowConcurrentExecution
public class ByeWorldJob extends QuartzJobBean {

	@Autowired
	private JobsService jobsService;

	@Override
	protected void executeInternal(JobExecutionContext context) {
		jobsService.byeWorld();
	}
}
