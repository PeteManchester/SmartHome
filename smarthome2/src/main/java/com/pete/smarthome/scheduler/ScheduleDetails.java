package com.pete.smarthome.scheduler;

import java.util.Calendar;

public class ScheduleDetails {

	private String device = "";
	private boolean randomOn = false;
	private boolean randomOff = false;
	private Calendar dateOn = null;
	private Calendar dateOff = null;
	private boolean nextDay = false;
	private String onCode = "";
	private String offCode ="";
	

	/**
	 * @return the device
	 */
	public String getDevice() {
		return device;
	}

	/**
	 * @param device
	 *            the device to set
	 */
	public void setDevice(String device) {
		this.device = device;
	}

	/**
	 * @return the randomOn
	 */
	public boolean isRandomOn() {
		return randomOn;
	}

	/**
	 * @param randomOn
	 *            the randomOn to set
	 */
	public void setRandomOn(boolean randomOn) {
		this.randomOn = randomOn;
	}

	/**
	 * @return the randomOff
	 */
	public boolean isRandomOff() {
		return randomOff;
	}

	/**
	 * @param randomOff
	 *            the randomOff to set
	 */
	public void setRandomOff(boolean randomOff) {
		this.randomOff = randomOff;
	}

	/**
	 * @return the dateOn
	 */
	public Calendar getDateOn() {
		return dateOn;
	}

	/**
	 * @param dateOn
	 *            the dateOn to set
	 */
	public void setDateOn(Calendar dateOn) {
		this.dateOn = dateOn;
	}

	public Calendar getDateOff() {
		return dateOff;
	}

	public void setDateOff(Calendar dateOff) {
		this.dateOff = dateOff;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ScheduleDetails [device=");
		builder.append(device);
		builder.append(", randomOn=");
		builder.append(randomOn);
		builder.append(", randomOff=");
		builder.append(randomOff);
		builder.append(", dateOn=");
		if (dateOn != null) {
			builder.append(dateOn.getTime().toString());
		} else {
			builder.append("Not Yet Set");
		}
		builder.append(", dateOff=");
		if (dateOff != null) {
			builder.append(dateOff.getTime().toString());
		} else {
			builder.append("Not Yet Set");
		}
		builder.append(", nextDay=");
		builder.append(nextDay);
		builder.append(", OnCode=");
		builder.append(onCode);
		builder.append(", OffCode=");
		builder.append(offCode);
		builder.append("]");
		return builder.toString();
	}

	public void setNextDay(boolean nextDay) {
		this.nextDay  = nextDay;
		
	}

	/**
	 * @return the nextDay
	 */
	public boolean isNextDay() {
		return nextDay;
	}

	/**
	 * @return the onCode
	 */
	public String getOnCode() {
		return onCode;
	}

	/**
	 * @param onCode the onCode to set
	 */
	public void setOnCode(String onCode) {
		this.onCode = onCode;
	}

	/**
	 * @return the offCode
	 */
	public String getOffCode() {
		return offCode;
	}

	/**
	 * @param offCode the offCode to set
	 */
	public void setOffCode(String offCode) {
		this.offCode = offCode;
	}

}
