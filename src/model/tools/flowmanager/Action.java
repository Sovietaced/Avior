package model.tools.flowmanager;

public class Action {

	String type, param, value;

	public Action(String t) {

		if (t.equals("output"))
			this.param = "Port";
		if (t.equals("enqueue"))
			this.param = "Port:Queue ID";
		if (t.equals("set-vlan-id"))
			this.param = "VLAN ID";
		if (t.equals("set-vlan-priority"))
			this.param = "VLAN PCP";
		if (t.equals("set-src-mac"))
			this.param = "Data Layer Address";
		if (t.equals("set-dst-mac"))
			this.param = "Data Layer Address";
		if (t.equals("set-tos-bits"))
			this.param = "Network Type Of Service";
		if (t.equals("set-src-ip"))
			this.param = "Network Address";
		if (t.equals("set-dst-ip"))
			this.param = "Network Address";
		if (t.equals("set-src-port"))
			this.param = "Transport Port";
		if (t.equals("set-dst-port"))
			this.param = "Transport Port";
		this.type = t;
	}

	public Action(String t, String v) {
		if (t.equals("output"))
			this.param = "Port";
		if (t.equals("enqueue"))
			this.param = "Port:Queue ID";
		if (t.equals("set-vlan-id"))
			this.param = "VLAN ID";
		if (t.equals("set-vlan-priority"))
			this.param = "VLAN PCP";
		if (t.equals("set-src-mac"))
			this.param = "Data Layer Address";
		if (t.equals("set-dst-mac"))
			this.param = "Data Layer Address";
		if (t.equals("set-tos-bits"))
			this.param = "Network Type Of Service";
		if (t.equals("set-src-ip"))
			this.param = "Network Address";
		if (t.equals("set-dst-ip"))
			this.param = "Network Address";
		if (t.equals("set-src-port"))
			this.param = "Transport Port";
		if (t.equals("set-dst-port"))
			this.param = "Transport Port";

		this.type = t;
		this.value = v;
	}

	public Action(String t, String v, String p) {
		this.type = t;
		this.value = v;
		this.param = p;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String serialize() {
		if (type.equals("strip-vlan")) {
			return type;
		}
		return type + "=" + value;
	}
}
