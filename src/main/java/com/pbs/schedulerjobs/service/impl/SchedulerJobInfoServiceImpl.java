package com.pbs.schedulerjobs.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pbs.schedulerjobs.model.SchedulerJobInfo;
import com.pbs.schedulerjobs.repository.SchedulerJobInfoRepository;
import com.pbs.schedulerjobs.service.SchedulerJobInfoService;

@Transactional
@Service
public class SchedulerJobInfoServiceImpl implements SchedulerJobInfoService {

  @Autowired private SchedulerJobInfoRepository schedulerJobInfoRepository;

  @Override
  public SchedulerJobInfo getSchedulerJobInfoById(Long idJobInfo) {
    return schedulerJobInfoRepository.getOne(idJobInfo);
  }

  @Override
  public List<SchedulerJobInfo> getAllJobs() {
    return schedulerJobInfoRepository.findAll();
  }
}
