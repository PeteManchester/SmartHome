package com.pete.startup;


import org.apache.logging.log4j.status.StatusLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pete.smarthome.jetty.JettyWebServer;
import com.pete.smarthome.scheduler.ScheduleController;

public class StartMe {
    public static void main(String[] args) throws Exception {    	
    	Logger log = LoggerFactory.getLogger("StartUp");
    	log.debug("Starting Scheduler");
    	ScheduleController.getInstance();
        log.debug("Starting Jetty");
        JettyWebServer ws = new JettyWebServer();
        StatusLogger.getLogger().reset();
        log.debug("Started Jetty");
    }
}