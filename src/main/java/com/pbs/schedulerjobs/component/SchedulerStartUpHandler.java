package com.pbs.schedulerjobs.component;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.pbs.schedulerjobs.service.SchedulerService;

@Log4j2
@Component
public class SchedulerStartUpHandler implements ApplicationRunner {

  @Autowired private SchedulerService schedulerService;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    log.info(
        "Programación de todas la planificaciones de jobs al iniciar - starting");
    try {
      schedulerService.startAllSchedulers();
      log.info(
          "Programación de todas la planificaciones de jobs al iniciar - complete");
    } catch (Exception ex) {
      log.error(
          "Programación de todas la planificaciones de jobs al iniciar - error", ex);
    }
  }
}
