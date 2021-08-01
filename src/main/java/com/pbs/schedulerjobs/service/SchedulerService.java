package com.pbs.schedulerjobs.service;

import com.pbs.schedulerjobs.model.SchedulerJobInfo;

public interface SchedulerService {

  void startAllSchedulers();

  void scheduleNewJob(SchedulerJobInfo jobInfo);

  void updateScheduleJob(SchedulerJobInfo jobInfo);

  boolean unScheduleJob(String jobName);

  boolean deleteJob(SchedulerJobInfo jobInfo);

  boolean pauseJob(SchedulerJobInfo jobInfo);

  boolean resumeJob(SchedulerJobInfo jobInfo);

  boolean startJobNow(SchedulerJobInfo jobInfo);

  boolean isJobPresent(SchedulerJobInfo jobInfo);

  boolean isJobRunning(SchedulerJobInfo jobInfo);
}
