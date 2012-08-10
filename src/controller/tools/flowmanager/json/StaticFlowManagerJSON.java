package controller.tools.flowmanager.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.tools.flowmanager.Flow;

import view.Gui;
import controller.util.Deserializer;
import controller.util.JSONArray;
import controller.util.JSONException;
import controller.util.JSONObject;

public class StaticFlowManagerJSON {

	static String IP = Gui.IP;
	static JSONObject jsonobj, obj;
	static JSONArray json;

	// This parses JSON from the restAPI to get all the flows from a switch
	public static List<Flow> getFlows(String sw) throws IOException,
			JSONException {
		
		List<Flow> flows = new ArrayList<Flow>();
		
		// Get the string names of all the specified switch's flows
		jsonobj = Deserializer.readJsonObjectFromURL("http://" + IP
				+ ":8080/wm/staticflowentrypusher/list/" + sw + "/json");
		
		if (!jsonobj.isNull(sw)){
			jsonobj = jsonobj.getJSONObject(sw);
			// Get the keys for the JSON Object
			Iterator<?> myIter = jsonobj.keys();
			// If a key exists, get the JSON Object for that key and create a flow using that object
			while (myIter.hasNext()) {
				try {
					String key = (String) myIter.next();
					if(jsonobj.has(key)){
					if (jsonobj.get(key) instanceof JSONObject) {
						obj = (JSONObject) jsonobj.get(key);
						Flow flow = new Flow();
						flow.setSwitch(sw);
						flow.setName(key);
						// Get the actions
						flow.setActions(ActionManagerJSON.getActions(sw, key));
						// Get the match
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
