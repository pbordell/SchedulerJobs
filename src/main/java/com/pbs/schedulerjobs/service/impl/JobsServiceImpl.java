package com.pbs.schedulerjobs.service.impl;

import org.springframework.stereotype.Service;

import com.pbs.schedulerjobs.service.JobsService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class JobsServiceImpl implements JobsService {

  public void helloWorld() {
	  log.info("Executing job Hello world");
  }

  @Override
  public void byeWorld() {
	  log.info("Executing job Bye world");
  }
}
