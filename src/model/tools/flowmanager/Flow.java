package model.tools.flowmanager;

import java.util.ArrayList;
import java.util.List;

public class Flow {

	String name, priority, cookie, idleTimeOut, hardTimeOut, outPort, sw;
	List<Action> actions = new ArrayList<Action>();
	Match match = new Match();

	public Flow() {
	}

	public Flow(String selectedSwitch) {
		sw = selectedSwitch;
	}

	public String getSwitch() {
		return sw;
	}

	public void setSwitch(String sw) {
		this.sw = sw;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	public String getIdleTimeOut() {
		return idleTimeOut;
	}

	public void setIdleTimeOut(String idleTimeOut) {
		this.idleTimeOut = idleTimeOut;
	}

	public String getHardTimeOut() {
		return hardTimeOut;
	}

	public void setHardTimeOut(String hardTimeOut) {
		this.hardTimeOut = hardTimeOut;
	}

	public String getOutPort() {
		return outPort;
	}

	public void setOutPort(String outPort) {
		this.outPort = outPort;
	}

	public List<Action> getActions() {
		return actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
	}

	public Match getMatch() {
		return match;
	}

	public void setMatch(Match match) {
		this.match = match;
	}

	public String serialize() {
		String serial = "{";

		if (sw != null) {
			if (serial.length() > 15)
				serial = serial.concat(", ");
			serial = serial.concat("\"switch\": \"" + sw + "\"");
		}
		if (name != null) {
			if (serial.length() > 15)
				serial = serial.concat(", ");
			serial = serial.concat("\"name\": \"" + name + "\"");
		}
		serial = serial.concat(", \"active\":\"true\"");
		if (cookie != null) {
			if (serial.length() > 15)
				serial = serial.concat(", ");
			serial = serial.concat("\"cookie\": \"" + cookie + "\"");
		}
		if (priority != null) {
			if (serial.length() > 15)
				serial = serial.concat(", ");
			serial = serial.concat("\"priority\": \"" + priority + "\"");
		}
		if (idleTimeOut != null) {
			if (serial.length() > 15)
				serial = serial.concat(", ");
			serial = serial.concat("\"idleTimout\": \"" + idleTimeOut + "\"");
		}
		if (hardTimeOut != null) {
			if (serial.length() > 15)
				serial = serial.concat(", ");
			serial = serial.concat("\"hardTimeout\": \"" + hardTimeOut + "\"");
		}
		if (outPort != null) {
			if (serial.length() > 15)
				serial = serial.concat(", ");
			serial = serial.concat("\"outPort\": \"" + outPort + "\"");
		}
		if (!actions.isEmpty()) {
			if (serial.length() > 15)
				serial = serial.concat(", ");
			serial = serial.concat("\"actions\": \"");
			int len = serial.length();
			for (Action act : actions) {
				if (serial.length() > len + 1)
					serial = serial.concat(",");
				serial = serial.concat(act.serialize());
			}
			serial = serial.concat("\"");
		}
		if (match != null) {
			serial = serial.concat(match.Serialize());
		}

		serial = serial.concat("}");
		return serial.toString();
	}

	public String deleteString() {
		return "{\"name\":\"" + name + "\"}";
	}
}
