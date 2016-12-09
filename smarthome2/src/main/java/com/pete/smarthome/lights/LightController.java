package com.pete.smarthome.lights;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pete.smarthome.comms.SmartPhone;

public class LightController {

	private static LightController instance = null;
	Logger log = LoggerFactory.getLogger(this.getClass());

	String light_on = "1381717";
	String light_off = "1381716";
	String path = "/home/pi/433Utils/RPi_utils/codesend";
	Timer timer = null;
	boolean isLightOn = false;

	public static LightController getInstance() {
		if (instance == null) {
			instance = new LightController();
		}
		return instance;
	}

	private LightController() {

	}

	public void LightsOn(String value) {
		isLightOn = true;
		BuildCommand(value);
		//SendNotificationNMA("Lights ON. " + value);
		//SendNotificationProwl("Lights On: " + value);
	}

	public void LightsOff(String value) {
		isLightOn = false;
		BuildCommand(value);
		//SendNotificationNMA("Lights OFF. " + value);
		//SendNotificationProwl("Lights Off: " + value);
	}

	private void BuildCommand(String value) {
		String command = "";
		if (isLightOn) {
			if (value.equalsIgnoreCase("GARDEN")) {
				command = "1381717";
			} else if (value.equalsIgnoreCase("BEDROOM")) {
				command = "1397077";
			} else if (value.equalsIgnoreCase("XMAS")) {
				command = "15028159";
			} else if(value.equalsIgnoreCase("GARAGE"))
			{
				command = "1397845";
			}
		} else {
			if (value.equalsIgnoreCase("GARDEN")) {
				command = "1381716";
			} else if (value.equalsIgnoreCase("BEDROOM")) {
				command = "1397076";
			} else if (value.equalsIgnoreCase("XMAS")) {
				command = "15028151";
			} else if (value.equalsIgnoreCase("GARAGE"))
			{
				command = "1397844";
			}
		}
		SendCommand(command);
	}

	public void SendCommand(String command) {
		List<String> params = new ArrayList<String>();
		params.add(path);
		params.add(command);
		log.debug("Send: " + command);

		ProcessBuilder builder = new ProcessBuilder(params);
		builder.redirectErrorStream(true);
		try {
			Process process = builder.start();
		} catch (IOException e) {
			log.debug("Cannot run command: " + e.getMessage());
		}
	}

	public void LightsFlash() {

	}
	


	public void doorbell() {
		SmartPhone sp = new SmartPhone();
		sp.SendNotificationProwl("DoorBell",1);
		
	}


}