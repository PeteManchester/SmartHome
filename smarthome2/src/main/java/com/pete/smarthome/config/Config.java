package com.pete.smarthome.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

//http://www.jsonschema2pojo.org/

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "longitude", "latitude", "devices" })
public class Config {

	@JsonProperty("longitude")
	private double longitude;
	@JsonProperty("latitude")
	private double latitude;
	@JsonProperty("devices")
	private List<Device> devices = null;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("longitude")
	public double getLongitude() {
		return longitude;
	}

	@JsonProperty("longitude")
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	@JsonProperty("latitude")
	public double getLatitude() {
		return latitude;
	}

	@JsonProperty("latitude")
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	@JsonProperty("devices")
	public List<Device> getDevices() {
		return devices;
	}

	@JsonProperty("devices")
	public void setDevices(List<Device> devices) {
		this.devices = devices;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Config [longitude=");
		builder.append(longitude);
		builder.append(", latitude=");
		builder.append(latitude);
		builder.append(", devices=");
		builder.append(devices);
		builder.append(", additionalProperties=");
		builder.append(additionalProperties);
		builder.append("]");
		return builder.toString();
	}

}