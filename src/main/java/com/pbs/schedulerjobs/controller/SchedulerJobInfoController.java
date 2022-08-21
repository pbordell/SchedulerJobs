package com.pbs.schedulerjobs.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.jxls.template.SimpleExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.altran.galileu.endpoints.SchedulerJobInfoDTO;
import com.pbs.schedulerjobs.constants.EndpointConstant;
import com.pbs.schedulerjobs.model.SchedulerJobInfo;
import com.pbs.schedulerjobs.service.SchedulerJobInfoService;
import com.pbs.schedulerjobs.service.SchedulerService;

@Controller
@RequestMapping(EndpointConstant.SCHEDULER_JOB_INFO_PATH)
public class SchedulerJobInfoController {

	@Autowired
	private SchedulerJobInfoService schedulerJobInfoService;

	@Autowired
	private SchedulerService schedulerService;

	@GetMapping(value = { "/", "/index" })
	public String index(Model model) {
		return "index";
	}

	@GetMapping(path = EndpointConstant.GET_ALL_JOBS_PATH)
	public String getAllJobs(Model model) {
		List<SchedulerJobInfo> result = schedulerJobInfoService.getAllJobs();
		model.addAttribute(EndpointConstant.PARAM_JOBS, result);
		model.addAttribute(EndpointConstant.PARAM_SCHEDULER_JOB_INFO, new SchedulerJobInfo());
		return "schedulerjobs";
	}

	@SuppressWarnings("rawtypes")
	@GetMapping(path = EndpointConstant.INICIAR_JOB_PATH)
	public ResponseEntity iniciarJob(@RequestParam(EndpointConstant.PARAM_JOB_ID_PATH) Long jobId) {
		SchedulerJobInfo schedulerJobInfo = schedulerJobInfoService.getSchedulerJobInfoById(jobId);
		if (schedulerJobInfo != null) {
			if (!schedulerService.isJobRunning(schedulerJobInfo)) {
				boolean status = schedulerService.startJobNow(schedulerJobInfo);
				if (status) {
					return new ResponseEntity<>(HttpStatus.OK);
				}
			}
		}
		
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	@PostMapping(path = EndpointConstant.CREAR_JOB_PATH)
	public String crearJob(@RequestBody SchedulerJobInfo schedulerJobInfo) {
	    schedulerService.scheduleNewJob(schedulerJobInfo);
        return "redirect:" + EndpointConstant.SCHEDULER_JOB_INFO_PATH + EndpointConstant.GET_ALL_JOBS_PATH;
	}
	
	@PostMapping(path = EndpointConstant.EDIT_JOB_PATH)
	public String editJob(@ModelAttribute SchedulerJobInfo schedulerJobInfo, Model model) {
		SchedulerJobInfo schedulerJobInfoBd = schedulerJobInfoService.getSchedulerJobInfoById(schedulerJobInfo.getId());
		schedulerJobInfoBd.setCronExpression(schedulerJobInfo.getCronExpression());
		schedulerService.updateScheduleJob(schedulerJobInfoBd);
        return "redirect:" + EndpointConstant.SCHEDULER_JOB_INFO_PATH + EndpointConstant.GET_ALL_JOBS_PATH;
	}
	
	@GetMapping(path = EndpointConstant.EXPORT_JOB_EXCEL)
	public void export(HttpServletResponse response) throws IOException {
		List<SchedulerJobInfo> listJobs = schedulerJobInfoService.getAllJobs();

		response.addHeader("Content-disposition", "attachment; filename=Jobs_" + LocalDateTime.now() + ".xls");
		response.setContentType("application/vnd.ms-excel");
		new SimpleExporter().gridExport(EndpointConstant.HEADER_EXPORT_EXCEL_JOB, listJobs,
				EndpointConstant.DATA_EXPORT_EXCEL_JOB, response.getOutputStream());
		response.flushBuffer();

	}
}
