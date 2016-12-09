package com.pete.smarthome.comms;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmartPhone {

	Logger log = LoggerFactory.getLogger(this.getClass());

	public void SendNotificationProwl(String message, int priority) {
		
		try
		{
			String m = java.net.URLEncoder.encode(message, "UTF-8");
			String USER_AGENT = "Mozilla/5.0";
			String url = "https://api.prowlapp.com/publicapi/add?";
			String parameters = "apikey=8e8ff105f5380c9f4d75e4d1518cf50750167cf5&application=SmartHome&event=%1$s&description=%2$s&priority=%3$s";
			parameters = String.format(parameters, URLEncoder.encode(message, "UTF-8"),
					URLEncoder.encode(InetAddress.getLocalHost().getHostName() + " Made Requet: " + message, "UTF-8"),
					priority);

			URL obj = new URL(url + parameters);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

			// add reuqest header
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

			String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";

			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();
			log.debug("Sending 'POST' request to URL : " + url);
			log.debug("Post parameters : " + urlParameters);
			log.debug("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			log.debug(response.toString());
		} catch (Exception e) {
			log.error("", e);
		}

	}

	public void SendNotificationProwl(String message) {
		SendNotificationProwl(message, 0);
	}

	public void SendNotificationNMA(String message) {
		// https://www.notifymyandroid.com/publicapi/notify?apikey=843c043813467c51c98369500144313630283a113ebe9cc5&application=DoorBell&event=Ding%20Dong&description=Description
		try

		{

			String m = java.net.URLEncoder.encode(message, "UTF-8");
			String USER_AGENT = "Mozilla/5.0";
			String url = "https://www.notifymyandroid.com/publicapi/notify?apikey=843c043813467c51c98369500144313630283a113ebe9cc5&application=DoorBell&description=Description&event="
					+ m;
			URL obj = new URL(url);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

			// add reuqest header
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

			String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";

			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();
			log.debug("\nSending 'POST' request to URL : " + url);
			log.debug("Post parameters : " + urlParameters);
			log.debug("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			log.debug(response.toString());
		} catch (Exception e) {
			log.error("", e);
		}
	}

}
