package com.pbs.schedulerjobs.service;

import java.util.List;

import com.pbs.schedulerjobs.model.SchedulerJobInfo;

public interface SchedulerJobInfoService {

  SchedulerJobInfo getSchedulerJobInfoById(Long idJobInfo);

  List<SchedulerJobInfo> getAllJobs();
  
}
