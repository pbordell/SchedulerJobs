package com.pbs.schedulerjobs.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "SCHEDULER_JOB_INFO")
public class SchedulerJobInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_ID_SCHEDULER_JOB_INFO")
  @SequenceGenerator(name = "SEQ_ID_SCHEDULER_JOB_INFO", sequenceName = "SEQ_ID_SCHEDULER_JOB_INFO")
  private Long id;

  @Column(name = "JOB_NOM", length = 50, nullable = false)
  private String jobName;

  @Column(name = "JOB_GRUP", length = 50, nullable = false)
  private String jobGroup;

  @Column(name = "JOB_CLASS", length = 50)
  private String jobClass;

  @Column(name = "CRON_EXP", length = 50)
  private String cronExpression;
}
