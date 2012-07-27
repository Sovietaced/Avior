package controller.tools.flowmanager.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.tools.flowmanager.Flow;

import view.Gui;
import view.tools.flowmanager.FlowManager;
import controller.util.Deserializer;
import controller.util.JSONArray;
import controller.util.JSONException;
import controller.util.JSONObject;

public class FlowManagerJSON {

	static String IP = Gui.IP;
	static JSONObject obj;
	static JSONArray json;

	public static List<Flow> getFlows(String sw) throws IOException,
			JSONException {
		
		List<Flow> flows = new ArrayList<Flow>();
		
		// Get the string names of all the specified switch's flows
		obj = Deserializer.readJsonObjectFromURL("http://" + IP
				+ ":8080/wm/staticflowentrypusher/list/" + sw + "/json");
		
		if (!obj.isNull(sw)){
			obj = obj.getJSONObject(sw);
			Iterator<?> myIter = obj.keys();
			while (myIter.hasNext()) {
				try {
					String key = (String) myIter.next();
					if(obj.has(key)){
					if (obj.get(key) instanceof JSONObject) {
						obj = (JSONObject) obj.get(key);
						Flow flow = new Flow();
						flow.setSwitch(sw);
						flow.setName(key);
						flow.setActions(ActionManagerJSON.getActions(sw, key));
						flow.setMatch(MatchManagerJSON.getMatch(sw, key));
						if (obj.getInt("priority") != 32767)
							flow.setPriority(String.valueOf(obj
									.getInt("priority")));
						flow.setCookie(String.valueOf(obj.getLong("cookie")));
						if (obj.getInt("idleTimeout") != 0)
							flow.setIdleTimeOut(String.valueOf(obj
									.getInt("idleTimeout")));
						if (obj.getInt("hardTimeout") != 0)
							flow.setHardTimeOut(String.valueOf(obj
									.getInt("hardTimeout")));
						if (obj.getInt("outPort") != -1)
							flow.setOutPort(String.valueOf(obj
									.getInt("outPort")));
						System.out.println(flow.serialize());
						flows.add(flow);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return flows;
	}
}
