package com.pbs.schedulerjobs.service.impl;

import lombok.extern.log4j.Log4j2;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import com.pbs.schedulerjobs.component.JobScheduleCreator;
import com.pbs.schedulerjobs.model.SchedulerJobInfo;
import com.pbs.schedulerjobs.repository.SchedulerJobInfoRepository;
import com.pbs.schedulerjobs.service.SchedulerService;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Log4j2
@Transactional
@Service
@SuppressWarnings("unchecked")
public class SchedulerServiceImpl implements SchedulerService {

	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;

	@Autowired
	private SchedulerJobInfoRepository schedulerJobInfoRepository;

	@Autowired
	private ApplicationContext context;

	@Autowired
	private JobScheduleCreator scheduleCreator;

	@Override
	public void startAllSchedulers() {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();

		schedulerJobInfoRepository.findAll().forEach(jobInfo -> {
			try {
				JobDetail jobDetail = JobBuilder
						.newJob((Class<? extends QuartzJobBean>) Class.forName(jobInfo.getJobClass()))
						.withIdentity(jobInfo.getJobName(), jobInfo.getJobGroup()).build();
				if (!scheduler.checkExists(jobDetail.getKey())) {
					jobDetail = scheduleCreator.createJob(
							(Class<? extends QuartzJobBean>) Class.forName(jobInfo.getJobClass()), false, context,
							jobInfo.getJobName(), jobInfo.getJobGroup());

					Trigger trigger;
					if (jobInfo.getCronExpression() != null
							&& CronExpression.isValidExpression(jobInfo.getCronExpression())) {
						trigger = scheduleCreator.createCronTrigger(jobInfo.getJobName(), new Date(),
								jobInfo.getCronExpression(), SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
					} else {
						trigger = scheduleCreator.createSimpleTrigger(jobInfo.getJobName(), new Date(),
								SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
					}

					scheduler.scheduleJob(jobDetail, trigger);
				}
			} catch (ClassNotFoundException e) {
				log.error("Class Not Found - {}", jobInfo.getJobClass(), e);
			} catch (SchedulerException e) {
				log.error(e.getMessage(), e);
			}
		});
	}

	@Override
	public void scheduleNewJob(SchedulerJobInfo jobInfo) {
		try {
			Scheduler scheduler = schedulerFactoryBean.getScheduler();

			JobDetail jobDetail = JobBuilder
					.newJob((Class<? extends QuartzJobBean>) Class.forName(jobInfo.getJobClass()))
					.withIdentity(jobInfo.getJobName(), jobInfo.getJobGroup()).build();
			if (!scheduler.checkExists(jobDetail.getKey())) {

				jobDetail = scheduleCreator.createJob(
						(Class<? extends QuartzJobBean>) Class.forName(jobInfo.getJobClass()), false, context,
						jobInfo.getJobName(), jobInfo.getJobGroup());

				Trigger trigger = scheduleCreator.createCronTrigger(jobInfo.getJobName(), new Date(),
						jobInfo.getCronExpression(), SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);

				scheduler.scheduleJob(jobDetail, trigger);
				schedulerJobInfoRepository.save(jobInfo);
			} else {
				log.error("scheduleNewJobRequest.jobAlreadyExist");
			}
		} catch (ClassNotFoundException e) {
			log.error("Class Not Found - {}", jobInfo.getJobClass(), e);
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
		}
	}

	@Override
	public void updateScheduleJob(SchedulerJobInfo jobInfo) {
		Trigger newTrigger = scheduleCreator.createCronTrigger(jobInfo.getJobName(), new Date(),
				jobInfo.getCronExpression(), SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
		try {
			schedulerFactoryBean.getScheduler().rescheduleJob(TriggerKey.triggerKey(jobInfo.getJobName()), newTrigger);
			schedulerJobInfoRepository.save(jobInfo);
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
		}
	}

	@Override
	public boolean unScheduleJob(String jobName) {
		try {
			return schedulerFactoryBean.getScheduler().unscheduleJob(new TriggerKey(jobName));
		} catch (SchedulerException e) {
			log.error("Failed to un-schedule job - {}", jobName, e);
			return false;
		}
	}

	@Override
	public boolean deleteJob(SchedulerJobInfo jobInfo) {
		try {
			return schedulerFactoryBean.getScheduler()
					.deleteJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
		} catch (SchedulerException e) {
			log.error("Failed to delete job - {}", jobInfo.getJobName(), e);
			return false;
		}
	}

	@Override
	public boolean pauseJob(SchedulerJobInfo jobInfo) {
		try {
			schedulerFactoryBean.getScheduler().pauseJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
			return true;
		} catch (SchedulerException e) {
			log.error("Failed to pause job - {}", jobInfo.getJobName(), e);
			return false;
		}
	}

	@Override
	public boolean resumeJob(SchedulerJobInfo jobInfo) {
		try {
			schedulerFactoryBean.getScheduler().resumeJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
			return true;
		} catch (SchedulerException e) {
			log.error("Failed to resume job - {}", jobInfo.getJobName(), e);
			return false;
		}
	}

	@Override
	public boolean startJobNow(SchedulerJobInfo jobInfo) {
		try {
			JobKey jobKey = new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup());
			schedulerFactoryBean.getScheduler().triggerJob(jobKey);
			return true;
		} catch (SchedulerException e) {
			log.error("Failed to start new job - {}", jobInfo.getJobName(), e);
			return false;
		}
	}

	@Override
	public boolean isJobPresent(SchedulerJobInfo jobInfo) {

		try {
			Scheduler scheduler = schedulerFactoryBean.getScheduler();
			JobKey jobKey = new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup());
			if (scheduler.checkExists(jobKey)) {
				return true;
			}
		} catch (SchedulerException e) {
			log.error("Failed to check job exist - {}", jobInfo.getJobName(), e);
		}
		return false;
	}

	@Override
	public boolean isJobRunning(SchedulerJobInfo jobInfo) {

		try {
			List<JobExecutionContext> currentJobs = schedulerFactoryBean.getScheduler().getCurrentlyExecutingJobs();
			if (currentJobs != null) {
				for (JobExecutionContext jobCtx : currentJobs) {
					String jobNameDB = jobCtx.getJobDetail().getKey().getName();
					String groupNameDB = jobCtx.getJobDetail().getKey().getGroup();
					if (jobInfo.getJobName().equalsIgnoreCase(jobNameDB)
							&& jobInfo.getJobGroup().equalsIgnoreCase(groupNameDB)) {
						return true;
					}
				}
			}
		} catch (SchedulerException e) {
			log.error("Failed to check job with key :" + jobInfo.getId() + " is running - {}", jobInfo.getJobName(), e);
			return false;
		}
		return false;
	}
}
