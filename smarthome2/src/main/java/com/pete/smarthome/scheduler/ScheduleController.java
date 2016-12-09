package com.pete.smarthome.scheduler;

import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pete.smarthome.lights.LightController;

public class ScheduleController {

	private static ScheduleController instance = null;
	Logger log = LoggerFactory.getLogger(this.getClass());

	private double latitude = 53.5445;
	private double longitude = -2.1187;

	Scheduler scheduler = null;

	private ScheduleController() {
		try {
			scheduler = StdSchedulerFactory.getDefaultScheduler();
			scheduler.start();
			SetSunSet(false);
		} catch (SchedulerException e) {
			log.error("Error ScheduleController", e);
		}
	}

	/***
	 * Create a schedule for when the sunsets and also for a t The SunsetTime
	 * 
	 * @param tommorow
	 */
	public void SetSunSet(boolean tommorow) {
		Calendar cal = Calendar.getInstance();
		cal = getSunSetTime(tommorow);
		CreateGarden(cal);
		CreateXMASLights(cal);
		CreateGarageLights(cal);
	}

	private Calendar getSunSetTime(boolean tommorow) {
		SunriseSunset ss = new SunriseSunset();
		Calendar now = Calendar.getInstance();
		if (tommorow) {
			now.roll(Calendar.DATE, true);
		}
		Date date = ss.getSunset(latitude, longitude, now.getTime());
		log.debug("SunSet Time: " + date.toString());
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}

	private void CreateGarden(Calendar cal) {
		ScheduleDetails sched = new ScheduleDetails();
		sched.setDevice("garden");
		sched.setDateOn(cal);
		sched.setNextDay(false);
		sched.setOnCode("1381717");
		sched.setOffCode("1381716");
		scheduleOn(sched);
	}

	private void CreateXMASLights(Calendar cal) {
		ScheduleDetails sched = new ScheduleDetails();
		sched.setDevice("xmas");
		sched.setDateOn(cal);
		sched.setNextDay(false);
		sched.setOnCode("15028159");
		sched.setOffCode("15028151");
		sched.setRandomOn(true);
		sched.setRandomOff(true);		
		scheduleOn(sched);
	}
	
	private void CreateGarageLights(Calendar cal) {
		ScheduleDetails sched = new ScheduleDetails();
		sched.setDevice("garage");
		sched.setDateOn(cal);
		sched.setNextDay(false);
		sched.setOnCode("1397845");
		sched.setOffCode("1397844");
		sched.setRandomOn(true);
		sched.setRandomOff(true);		
		scheduleOn(sched);
	}

	private int getRandomNumber() {
		int min = -300;
		int max = 300;
		int res = ThreadLocalRandom.current().nextInt(min, max + 1);
		log.debug("RandomNumber: " + res);
		return res;
	}

	public static ScheduleController getInstance() {
		if (instance == null) {
			instance = new ScheduleController();
		}
		return instance;
	}

	/***
	 * Create Schedule to turn on the Lights
	 * 
	 * @param device
	 */
	public void scheduleOn(ScheduleDetails details) {
		try

		{
			int offset = 0;
			String group = "GroupOn";
			Calendar date = details.getDateOn();
			if (details.isNextDay()) {
				log.debug("Get Tomorrows Sunset");
				date = getSunSetTime(true);
				log.debug("Tomorrows Sunset: " + date.getTime().toString());
			}

			if (details.isRandomOn()) {
				offset = getRandomNumber();
				date.add(Calendar.SECOND, offset);

			}
			details.setDateOn(date);
			log.info("Schedule Lights On: " + details.toString());
			String name = date.getTime().toString() + " " + details.getDevice() + " ON";
			JobDetail job = JobBuilder.newJob(TurnLightOn.class).withIdentity(name, group).build();
			Map dataMap = job.getJobDataMap();
			dataMap.put("details", details);
			dataMap.put("task", "ON");
			SimpleTrigger trigger = (SimpleTrigger) newTrigger().withIdentity(name, group).startAt(date.getTime()) // some
																													// Date
					.forJob(name, group) // identify job with name, group
											// strings
					.build();

			scheduler.scheduleJob(job, trigger);
			log.info("Scheduled Lights On: " + details.toString());
			scheduleOff(details);
		} catch (Exception e) {
			log.error("Error Creating Schedule", e);
		}

	}

	/***
	 * Create Schedule to turn off the lights
	 * 
	 * @param device
	 */
	public void scheduleOff(ScheduleDetails details) {
		String group = "GroupOff";
		int offset = 0;
		Calendar off = Calendar.getInstance();
		Calendar date = details.getDateOn();
		int year = date.get(Calendar.YEAR);
		int month = date.get(Calendar.MONTH);
		int day = date.get(Calendar.DAY_OF_MONTH);
		off.set(year, month, day, 23, 59, 00);

		if (details.isRandomOff()) {
			offset = getRandomNumber();
		}
		off.add(Calendar.SECOND, offset);
		details.setDateOff(off);
		log.info("Schedule Lights Off: " + details.toString());
		try

		{
			// log.info("Schedule Lights Off: " + date.getTime().toString());
			String name = off.getTime().toString() + " " + details.getDevice() + " OFF";
			JobDetail job = JobBuilder.newJob(TurnLightOff.class).withIdentity(name, group).build();
			Map dataMap = job.getJobDataMap();
			dataMap.put("details", details);
			dataMap.put("task", "OFF");
			SimpleTrigger trigger = (SimpleTrigger) newTrigger().withIdentity(name, group).startAt(off.getTime()) // some
																													// Date
					.forJob(name, group) // identify job with name, group
											// strings
					.build();
			scheduler.scheduleJob(job, trigger);
			log.info("Scheduled Lights Off: " + details.toString());
		} catch (Exception e) {
			log.error("Error Creating Schedule", e);
		}
	}

	/***
	 * Read the schedules
	 * 
	 * @return
	 */
	public String readSchedules() {
		StringBuilder res = new StringBuilder();
		try {
			for (String group : scheduler.getJobGroupNames()) {
				for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(group))) {
					log.debug("Found job identified by: " + jobKey);

					Trigger trigger = scheduler.getTrigger(new TriggerKey(jobKey.getName(), jobKey.getGroup()));
					// log.debug("Trigger Time: " +
					// trigger.getNextFireTime().toString());
					JobDetail jb = scheduler.getJobDetail(jobKey);
					ScheduleDetails details = (ScheduleDetails) jb.getJobDataMap().get("details");
					String task = jb.getJobDataMap().getString("task");
					// log.debug("JobDetail: " + device + " " + task);
					String sDetails = "JobName: " + jobKey.getName() + " GroupName: " + jobKey.getGroup() + details.toString() + " ScheduleTime: " + trigger.getNextFireTime().toString();
					log.debug("JobDetails: " + sDetails);
					res.append(sDetails);
					res.append("\r\n");
					res.append("\r\n");
				}
			}
		} catch (Exception e) {
			res.append("Error");
			log.error("Error Get Jobs", e);
		}
		return res.toString();
	}

	/***
	 * Get the Sunrise and Sunset times
	 * 
	 * @return
	 */
	public String getSunriseSunsetTimes() {
		String res = "";
		SunriseSunset ss = new SunriseSunset();
		Date sunrise = ss.getSunrise(latitude, longitude);
		Date sunset = ss.getSunset(latitude, longitude);
		res = "Sunrise: " + sunrise.toString();
		res += "\r\n";
		res += "Sunset: " + sunset.toString();
		return res;
	}

	public String doorbell() {
		LightController.getInstance().doorbell();
		return null;
	}

}
