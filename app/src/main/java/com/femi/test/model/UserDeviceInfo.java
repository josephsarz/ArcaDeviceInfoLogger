package com.femi.test.model;


public class UserDeviceInfo{

	private String dataUsageCollationTime;
	private String appVersion;
	private boolean hasApp;
	private int signalStrength ;
	private int numberOfApps;
	private Location location;
	private String terminalId;
	private String networkType;
	private String connectivityStatus;
	private int batteryLevel;

	public UserDeviceInfo(String dataUsageCollationTime, String appVersion, boolean hasApp, int signalStrength, int numberOfApps, Location location, String terminalId, String networkType, String connectivityStatus, int batteryLevel) {
		this.dataUsageCollationTime = dataUsageCollationTime;
		this.appVersion = appVersion;
		this.hasApp = hasApp;
		this.signalStrength = signalStrength;
		this.numberOfApps = numberOfApps;
		this.location = location;
		this.terminalId = terminalId;
		this.networkType = networkType;
		this.connectivityStatus = connectivityStatus;
		this.batteryLevel = batteryLevel;
	}

	@Override
	public String toString() {
		return "UserDeviceInfo{" +
				"dataUsageCollationTime='" + dataUsageCollationTime + '\'' +
				", appVersion='" + appVersion + '\'' +
				", hasApp=" + hasApp +
				", signalStrength=" + signalStrength +
				", numberOfApps=" + numberOfApps +
				", location=" + location +
				", terminalId='" + terminalId + '\'' +
				", networkType='" + networkType + '\'' +
				", connectivityStatus='" + connectivityStatus + '\'' +
				", batteryLevel=" + batteryLevel +
				'}';
	}
}