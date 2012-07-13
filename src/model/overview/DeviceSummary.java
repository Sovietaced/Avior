package model.overview;

import java.util.Date;

public class DeviceSummary {

	String macAddress, ipv4, attachedSwitch;
	int switchPort;
	Date lastSeen;

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public String getIpv4() {
		return this.ipv4;
	}

	public void setIpv4(String ipv4) {
		this.ipv4 = ipv4;
	}

	public String getAttachedSwitch() {
		return this.attachedSwitch;
	}

	public void setAttachedSwitch(String attachedSwitch) {
		this.attachedSwitch = attachedSwitch;
	}

	public int getSwitchPort() {
		return this.switchPort;
	}

	public void setSwitchPort(int switchPort) {
		this.switchPort = switchPort;
	}

	public Date getLastSeen() {
		return this.lastSeen;
	}

	public void setLastSeen(Date lastSeen) {
		this.lastSeen = lastSeen;
	}

	@Override
	public String toString() {
		return "Device: " + this.macAddress + " | IPv4: " + this.ipv4
				+ " | Attached Switch: " + this.attachedSwitch
				+ " | Switch Port: " + this.switchPort + " | Last Seen: "
				+ this.lastSeen;
	}

	public DeviceSummary(String mac) {
		macAddress = mac;
	}
}
