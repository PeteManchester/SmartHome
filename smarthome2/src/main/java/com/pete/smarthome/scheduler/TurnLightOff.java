package com.pete.smarthome.scheduler;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pete.smarthome.comms.SmartPhone;
import com.pete.smarthome.lights.LightController;

public class TurnLightOff implements Job {
	Logger log = LoggerFactory.getLogger(this.getClass());

	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.debug("Turn Off Lights");

		JobDataMap map = context.getJobDetail().getJobDataMap();
		if (map.containsKey("details")) {
			ScheduleDetails details = (ScheduleDetails) map.get("details");
			log.debug("Turn lights Off: " + details);
			if (details.isNotificationEnabled()) {
				SmartPhone sp = new SmartPhone();
				sp.SendNotificationProwl("Lights OFF. " + details);
			}
			LightController.getInstance().SendCommand(details.getOffCode());
		}
	}

}
