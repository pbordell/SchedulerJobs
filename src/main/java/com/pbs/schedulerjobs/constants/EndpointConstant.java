package com.pbs.schedulerjobs.constants;

import java.util.Arrays;
import java.util.List;

public final class EndpointConstant {
	
	public static final String SCHEDULER_PATH = "/scheduler";
	public static final String SCHEDULER_JOB_INFO_PATH = "/schedulerJobInfo";

	public static final String INICIAR_JOB_PATH = "/iniciarJob";
	public static final String GET_ALL_JOBS_PATH = "/getAllJobs";
	public static final String GET_JOB_PATH = "/getJob/{id}";
	public static final String EDIT_JOB_PATH = "/editJob";
	public static final String EXPORT_JOB_EXCEL = "/export/excel";
	public static final String TITLE_JOB_EXCEL = "Jobs";

	public static final String PARAM_JOB_ID_PATH = "jobId";
	public static final String PARAM_ID_PATH = "id";
	public static final String PARAM_JOBS = "allJobs";
	public static final String PARAM_SCHEDULER_JOB_INFO = "schedulerJobInfo";

	public static final List<String> HEADER_EXPORT_EXCEL_JOB = Arrays.asList("Job", "Frequence");
	public static final String DATA_EXPORT_EXCEL_JOB = "jobName, cronExpression";

	
	private EndpointConstant() {
		throw new UnsupportedOperationException();
	}
}
