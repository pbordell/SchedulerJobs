package com.pbs.schedulerjobs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pbs.schedulerjobs.model.SchedulerJobInfo;

@Repository
public interface SchedulerJobInfoRepository extends JpaRepository<SchedulerJobInfo, Long> {}
