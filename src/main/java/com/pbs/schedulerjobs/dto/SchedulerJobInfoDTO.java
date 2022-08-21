package com.pbs.schedulerjobs.dto;

import lombok.Data;

@Data
public class SchedulerJobInfoDTO {

  private Long id;

  private String jobName;

  private String jobGroup;

  private String jobClass;

  private String cronExpression;
}
