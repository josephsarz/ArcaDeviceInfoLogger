package com.femi.test.model;

public class Location{
	private String latitude;
	private String longitude;
	public String getLatitude(){
		return latitude;
	}
	public String getLongitude(){
		return longitude;
	}

	public Location(String latitude, String longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	@Override
	public String toString() {
		return "Location{" +
				"latitude='" + latitude + '\'' +
				", longitude='" + longitude + '\'' +
				'}';
	}
}