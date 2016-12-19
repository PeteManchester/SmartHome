package com.pete.smarthome.scheduler;

import static org.quartz.TriggerBuilder.newTrigger;

import java.io.File;
import java.text.SimpleDateFormat;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pete.smarthome.config.Config;
import com.pete.smarthome.config.Device;
import com.pete.smarthome.lights.LightController;

public class ScheduleController {

	private static ScheduleController instance = null;
	Logger log = LoggerFactory.getLogger(this.getClass());
	private Config config = null;

	// private double latitude = 53.5445;
	// private double longitude = -2.1187;

	Scheduler scheduler = null;

	private ScheduleController() {
		try {
			scheduler = StdSchedulerFactory.getDefaultScheduler();
			scheduler.start();
			getConfig();
			SetSunSet(false);
		} catch (SchedulerException e) {
			log.error("Error ScheduleController", e);
		}
	}

	private void getConfig() {

		try {
			ObjectMapper mapper = new ObjectMapper(); // can reuse, share
														// globally
			config = mapper.readValue(new File("devices.json"), Config.class);
			log.info("Config: " + config.toString());
		} catch (Exception e) {
			log.error("Error Reading Config", e);
		}
	}

	/***
	 * Create a schedule for when the sunsets and also for a t The SunsetTime
	 * 
	 * @param tommorow
	 */
	public void SetSunSet(boolean tommorow) {
		try {
			Calendar cal = Calendar.getInstance();
			cal = getSunSetTime(tommorow, config.getLatitude(), config.getLongitude());
			log.debug(config.toString());
			Device d = new Device();
			for (Device device : config.getDevices()) {
				device.setDateOn(cal);
				scheduleOn(device);
			}
		} catch (Exception e) {
			log.error("Error Creating Schedules", e);
		}
	}

	/**
	 * Get the Sunset Time
	 * 
	 * @param tommorow
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	private Calendar getSunSetTime(boolean tommorow, double latitude, double longitude) {
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

	/**
	 * 
	 * @param range
	 * @return
	 */
	private int getRandomNumber(String range) {
		String[] splits = range.split(":");
		int min = -300;
		int max = 300;
		if (splits.length > 1) {
			min = getInt(splits[0]);
			max = getInt(splits[1]);
		}

		int res = ThreadLocalRandom.current().nextInt(min, max + 1);
		log.debug("RandomNumber: " + res);
		return res;
	}

	/**
	 * 
	 * @param s
	 * @return
	 */
	private int getInt(String s) {
		try {
			return Integer.parseInt(s);
		} catch (Exception e) {
			log.error("Error Getting Int: " + s, e);
		}
		return 0;
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
	public void scheduleOn(Device details) {
		try {
			int offset = 0;
			String group = "GroupOn";
			Calendar date = details.getDateOn();
			if (details.isNextDay()) {
				log.debug("Get Tomorrows Sunset");
				date = getSunSetTime(true, config.getLatitude(), config.getLongitude());
				log.debug("Tomorrows Sunset: " + date.getTime().toString());
			}

			// if (details.isRandomOn()) {
			offset = getRandomNumber(details.getRandomOn());
			date.add(Calendar.SECOND, offset);

			// }
			details.setDateOn(date);
			log.info("Schedule Lights On: " + details.toString());
			String name = date.getTime().toString() + " " + details.getDevice() + " ON";
			JobDetail job = JobBuilder.newJob(TurnLightOn.class).withIdentity(name, group).build();
			Map dataMap = job.getJobDataMap();
			Device d = (Device) details.clone();
			dataMap.put("details", d);
			dataMap.put("task", "ON");
			SimpleTrigger trigger = (SimpleTrigger) newTrigger().withIdentity(name, group).startAt(date.getTime()).forJob(name, group).build();

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
	public void scheduleOff(Device details) {
		String group = "GroupOff";
		int offset = 0;
		Calendar off = Calendar.getInstance();
		Calendar date = details.getDateOn();
		int year = date.get(Calendar.YEAR);
		int month = date.get(Calendar.MONTH);
		int day = date.get(Calendar.DAY_OF_MONTH);

		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
		Date dTime = null;
		try {
			dTime = sdf.parse(details.getTimeOff());
		} catch (Exception e) {
			log.error("Error getting Off Time", e);
		}

		off.set(year, month, day, sdf.getCalendar().get(Calendar.HOUR_OF_DAY), sdf.getCalendar().get(Calendar.MINUTE), sdf.getCalendar().get(Calendar.SECOND));

		if (off.before(details.getDateOn())) {
			log.debug("Date Off is before Date On: " + details);
			off.roll(Calendar.DATE, true);
		}

		// if (details.isRandomOff()) {
		offset = getRandomNumber(details.getRandomOff());
		// }
		off.add(Calendar.SECOND, offset);
		details.setDateOff(off);
		log.info("Schedule Lights Off: " + details.toString());
		try

		{
			// log.info("Schedule Lights Off: " + date.getTime().toString());
			String name = off.getTime().toString() + " " + details.getDevice() + " OFF";
			JobDetail job = JobBuilder.newJob(TurnLightOff.class).withIdentity(name, group).build();
			Map dataMap = job.getJobDataMap();
			Device d = (Device) details.clone();
			dataMap.put("details", d);
			dataMap.put("task", "OFF");
			SimpleTrigger trigger = (SimpleTrigger) newTrigger().withIdentity(name, group).startAt(off.getTime()).forJob(name, group).build();
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
					JobDetail jb = scheduler.getJobDetail(jobKey);
					Device details = (Device) jb.getJobDataMap().get("details");
					// String task = jb.getJobDataMap().getString("task");
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
		Date sunrise = ss.getSunrise(config.getLatitude(), config.getLongitude());
		Date sunset = ss.getSunset(config.getLatitude(), config.getLongitude());
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
