package com.pete.smarthome.jetty;


import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pete.smarthome.rest.RestLights;

//http://nikgrozev.com/2014/10/16/rest-with-embedded-jetty-and-jersey-in-a-single-jar-step-by-step/

public class JettyWebServer {

	Logger log = LoggerFactory.getLogger(this.getClass());

	public JettyWebServer() {
		JettyWebServerNew();
	}

	public void JettyWebServerNew() {
		// http://stackoverflow.com/questions/37293015/rest-and-static-content-in-jetty-server
		Server jettyServer = new Server(80);

		// JSON Handler
		RestLights resource = new RestLights();

		ResourceConfig rc = new ResourceConfig();
		rc.register(resource);

		ServletContainer sc = new ServletContainer(rc);

		ServletHolder servletHolder = new ServletHolder(sc);

		ServletContextHandler jsonResourceContext = new ServletContextHandler();
		jsonResourceContext.addServlet(servletHolder, "/pete/*");

		// Start of static file handling

		ResourceHandler staticResourceHandler = new ResourceHandler();

		// 2.Setting Resource Base
		try {
			String webDir = this.getClass().getClassLoader().getResource("com/web").toExternalForm();
			staticResourceHandler.setResourceBase(webDir);
		} catch (Exception e) {
			log.error("Error Getting com/web",e);
		}

		// 3.Enabling Directory Listing

		staticResourceHandler.setDirectoriesListed(true);

		// 4.Setting Context Source

		ContextHandler staticContextHandler = new ContextHandler("/");

		// 5.Attaching Handlers

		staticContextHandler.setHandler(staticResourceHandler);

		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { staticContextHandler, jsonResourceContext, new DefaultHandler() });
		jettyServer.setHandler(handlers);

		// End Add Static file handling

		try {
			jettyServer.start();
			//jettyServer.join();
		} catch (Exception e) {
			log.error("Error Starting Jetty",e);
		} finally {
			//jettyServer.destroy();
		}
	}

	public void JettyWebServerOLD() {

		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");

		Server jettyServer = new Server(8080);
		jettyServer.setHandler(context);

		ServletHolder jerseyServlet = context.addServlet(org.glassfish.jersey.servlet.ServletContainer.class,
				"/pete/*");
		jerseyServlet.setInitOrder(0);

		// Tells the Jersey Servlet which REST service/class to load.
		jerseyServlet.setInitParameter("jersey.config.server.provider.classnames", RestLights.class.getCanonicalName());

		// Start of static file handling

		ResourceHandler resourceHandler = new ResourceHandler();

		// 2.Setting Resource Base

		String webDir = this.getClass().getClassLoader().getResource("com/web/files").toExternalForm();

		resourceHandler.setResourceBase(webDir);

		// 3.Enabling Directory Listing

		resourceHandler.setDirectoriesListed(true);

		// 4.Setting Context Source

		ContextHandler contextHandler = new ContextHandler("/jcg");

		// 5.Attaching Handlers

		contextHandler.setHandler(resourceHandler);

		// context.setHandler(contextHandler);

		// HandlerCollection handlerCollection = new HandlerCollection();
		// handlerCollection.setHandlers(new Handler[]{context});

		// jettyServer.setHandler(handlerCollection);
		// jettyServer.setHandler(contextHandler);

		// End Add Static file handling

		try {
			jettyServer.start();
			//jettyServer.join();
		} catch (Exception e) {
			log.error("Error Starting Jetty",  e);
		} finally {
			//jettyServer.destroy();
		}
	}

}
