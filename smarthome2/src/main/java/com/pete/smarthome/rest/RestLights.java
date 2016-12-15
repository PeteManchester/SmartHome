package com.pete.smarthome.rest;

import java.net.InetAddress;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pete.smarthome.lights.LightController;
import com.pete.smarthome.scheduler.ScheduleController;

@Path("/lightcontroller")
public class RestLights {

	Logger log = LoggerFactory.getLogger(this.getClass());

	@GET
	@Path("test")
	@Produces(MediaType.TEXT_PLAIN)
	public String test() {
		return "Test";
	}

	@GET
	@Path("on")
	@Produces(MediaType.TEXT_PLAIN)
	public String LightsOn(@QueryParam("value") String value, @Context HttpServletRequest req) {
		String host = req.getRemoteAddr();
		try {
			InetAddress addr = InetAddress.getByName(req.getRemoteAddr());
			host = addr.getHostName();
		} catch (Exception e) {
			log.error("Could Not Resovle IPAddress: " + host, e);
		}
		log.debug("On Request: " + value + " from: " + host);
		LightController.getInstance().LightsOn(value);
		return "ON";
	}

	@GET
	@Path("off")
	@Produces(MediaType.TEXT_PLAIN)
	public String LightsOff(@QueryParam("value") String value, @Context HttpServletRequest req) {
		String host = req.getRemoteAddr();
		try {
			InetAddress addr = InetAddress.getByName(req.getRemoteAddr());
			host = addr.getHostName();
		} catch (Exception e) {
			log.error("Could Not Resovle IPAddress: " + host, e);
		}

		log.debug("Off Request: " + value + " from: " + host);
		LightController.getInstance().LightsOff(value);
		return "OFF";
	}

	@GET
	@Path("flash")
	@Produces(MediaType.TEXT_PLAIN)
	public String LightsFlash() {
		log.debug("Flash Request");
		LightController.getInstance().LightsFlash();
		return "OFF";
	}

	@GET
	@Path("getSchedule")
	@Produces(MediaType.TEXT_PLAIN)
	public String GetSchedule() {
		log.debug("Get Schedule Request");
		String res = ScheduleController.getInstance().readSchedules();
		return res;
	}

	@GET
	@Path("getSunriseSunsetTimes")
	@Produces(MediaType.TEXT_PLAIN)
	public String GetSunriseSunsetTimes(@Context HttpHeaders headers) {
		log.debug("Get SunriseSunsetTimes Request: " + headers.getRequestHeader("user-agent").get(0));
		String res = ScheduleController.getInstance().getSunriseSunsetTimes();
		return res;
	}

	@GET
	@Path("doorbell")
	@Produces(MediaType.TEXT_PLAIN)
	public String Doorbell(@Context HttpHeaders headers) {
		log.debug("Get SunriseSunsetTimes Request: " + headers.getRequestHeader("user-agent").get(0));
		String res = ScheduleController.getInstance().doorbell();
		return res;
	}

}