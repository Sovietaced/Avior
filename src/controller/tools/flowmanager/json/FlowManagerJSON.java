package controller.tools.flowmanager.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.tools.flowmanager.Flow;

import view.Gui;
import avior.json.JSONArray;
import avior.json.JSONException;
import avior.json.JSONObject;
import controller.Deserializer;

public class FlowManagerJSON {

	static String IP = Gui.IP;
	static JSONObject obj;
	static JSONArray json;

	public static List<String> getSwitchList() throws JSONException,
			IOException {

		List<String> switchList = new ArrayList<String>();

		// Get the string IDs of all the switches
		json = Deserializer.readJsonArrayFromURL("http://" + IP
				+ ":8080/wm/core/controller/switches/json");

		for (int i = 0; i < json.length(); i++) {
			obj = json.getJSONObject(i);
			switchList.add(obj.getString("dpid"));
		}
		return switchList;
	}

	public static List<String> getFlowList(String dpid) throws JSONException,
			IOException {

		List<String> flowList = new ArrayList<String>();

		// Get the string names of all the specified switch's flows
		obj = Deserializer.readJsonObjectFromURL("http://" + IP
				+ ":8080/wm/staticflowentrypusher/list/" + dpid + "/json");
		obj = obj.getJSONObject(dpid);
		Iterator<?> myIter = obj.keys();
		while (myIter.hasNext()) {
			try {
				String key = (String) myIter.next();
				if (obj.get(key) instanceof JSONObject) {
					flowList.add(key);
				}
			} catch (Exception e) {
				System.out.println("swag sack");
			}
		}
		return flowList;
	}

	public static Flow getFlow(String dpid, String flowName)
			throws IOException, JSONException {
		// Get the string names of all the specified switch's flows
		obj = Deserializer.readJsonObjectFromURL("http://" + IP
				+ ":8080/wm/staticflowentrypusher/list/" + dpid + "/json");
		obj = obj.getJSONObject(dpid).getJSONObject(flowName);
		Flow flow = new Flow();
		flow.setSwitch(dpid);
		flow.setName(flowName);
		flow.setActions(ActionManagerJSON.getActions(dpid, flowName));
		flow.setMatch(MatchManagerJSON.getMatch(dpid, flowName));
		if (obj.getInt("priority") != 32767)
			flow.setPriority(String.valueOf(obj.getInt("priority")));
		flow.setCookie(String.valueOf(obj.getLong("cookie")));
		if (obj.getInt("idleTimeout") != 0)
			flow.setIdleTimeOut(String.valueOf(obj.getInt("idleTimeout")));
		if (obj.getInt("hardTimeout") != 0)
			flow.setHardTimeOut(String.valueOf(obj.getInt("hardTimeout")));
		if (obj.getInt("outPort") != -1)
			flow.setOutPort(String.valueOf(obj.getInt("outPort")));

		System.out.println("Debugging flow.serialize()" + flow.serialize());

		return flow;
	}
}
