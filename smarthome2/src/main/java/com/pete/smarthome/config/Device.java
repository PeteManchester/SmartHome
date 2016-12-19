
package com.pete.smarthome.config;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

//http://www.jsonschema2pojo.org/

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "device", "randomOn", "randomOff", "onCode", "offCode", "notificationEnabled", "timeOff" })
public class Device implements Cloneable {

	@JsonProperty("device")
	private String device;
	@JsonProperty("randomOn")
	private String randomOn = "0:0";
	@JsonProperty("randomOff")
	private String randomOff = "0:0";
	@JsonProperty("onCode")
	private String onCode;
	@JsonProperty("offCode")
	private String offCode;
	@JsonProperty("notificationEnabled")
	private Boolean notificationEnabled;
	@JsonProperty("timeOff")
	private String timeOff;
	@JsonProperty("dateOn")
	private Calendar dateOn = null;
	@JsonProperty("dateOff")
	private Calendar dateOff = null;
	@JsonProperty("nextDay")
	private boolean nextDay = false;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("device")
	public String getDevice() {
		return device;
	}

	@JsonProperty("device")
	public void setDevice(String device) {
		this.device = device;
	}

	@JsonProperty("randomOn")
	public String getRandomOn() {
		return randomOn;
	}

	@JsonProperty("randomOn")
	public void setRandomOn(String randomOn) {
		this.randomOn = randomOn;
	}

	@JsonProperty("randomOff")
	public String getRandomOff() {
		return randomOff;
	}

	@JsonProperty("randomOff")
	public void setRandomOff(String randomOff) {
		this.randomOff = randomOff;
	}

	@JsonProperty("onCode")
	public String getOnCode() {
		return onCode;
	}

	@JsonProperty("onCode")
	public void setOnCode(String onCode) {
		this.onCode = onCode;
	}

	@JsonProperty("offCode")
	public String getOffCode() {
		return offCode;
	}

	@JsonProperty("offCode")
	public void setOffCode(String offCode) {
		this.offCode = offCode;
	}

	@JsonProperty("notificationEnabled")
	public Boolean isNotificationEnabled() {
		return notificationEnabled;
	}

	@JsonProperty("notificationEnabled")
	public void setNotificationEnabled(Boolean notificationEnabled) {
		this.notificationEnabled = notificationEnabled;
	}

	@JsonProperty("timeOff")
	public String getTimeOff() {
		return timeOff;
	}

	@JsonProperty("timeOff")
	public void setTimeOff(String timeOff) {
		this.timeOff = timeOff;
	}

	@JsonProperty("dateOn")
	public Calendar getDateOn() {
		return dateOn;
	}

	@JsonProperty("dateOn")
	public void setDateOn(Calendar dateOn) {
		this.dateOn = dateOn;
	}

	@JsonProperty("dateOff")
	public Calendar getDateOff() {
		return dateOff;
	}

	@JsonProperty("dateOff")
	public void setDateOff(Calendar dateOff) {
		this.dateOff = dateOff;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	/**
	 * @return the nextDay
	 */
	@JsonProperty("nextDay")
	public boolean isNextDay() {
		return nextDay;
	}

	/**
	 * @param nextDay
	 *            the nextDay to set
	 */
	@JsonProperty("nextDay")
	public void setNextDay(boolean nextDay) {
		this.nextDay = nextDay;
	}

	/**
	 * Make Cloneable
	 */
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Device [device=");
		builder.append(device);
		builder.append(", randomOn=");
		builder.append(randomOn);
		builder.append(", randomOff=");
		builder.append(randomOff);
		builder.append(", onCode=");
		builder.append(onCode);
		builder.append(", offCode=");
		builder.append(offCode);
		builder.append(", notificationEnabled=");
		builder.append(notificationEnabled);
		builder.append(", timeOff=");
		builder.append(timeOff);
		builder.append(", dateOn=");
		if (dateOn != null) {
			builder.append(dateOn.getTime().toString());
		}
		builder.append(", dateOff=");
		if (dateOff != null) {
			builder.append(dateOff.getTime().toString());
		}
		builder.append(", nextDay=");
		builder.append(nextDay);
		builder.append(", additionalProperties=");
		builder.append(additionalProperties);
		builder.append("]");
		return builder.toString();
	}

}
