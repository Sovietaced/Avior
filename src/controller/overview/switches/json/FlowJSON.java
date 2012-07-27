package controller.overview.switches.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.tools.flowmanager.Flow;

import view.Gui;
import controller.util.Deserializer;
import controller.util.FormatLong;
import controller.util.JSONArray;
import controller.util.JSONException;
import controller.util.JSONObject;

public class FlowJSON {

	static String IP = Gui.IP;
	static JSONObject obj;
	static JSONArray json;

	public static List<Flow> getFlows(String sw) throws IOException,
			JSONException {
		
		List<Flow> flows = new ArrayList<Flow>();
		
		// Get the string names of all the specified switch's flows
		obj = Deserializer.readJsonObjectFromURL("http://" + IP
				+ ":8080/wm/core/switch/" + sw + "/flow/json");
		
		if (!obj.isNull(sw)){
			json = obj.getJSONArray(sw);
				for(int i = 0; i < json.length(); i++){
					obj = (JSONObject) json.get(i);
						Flow flow = new Flow(sw);
						flow.setActions(ActionJSON.getActions(obj.getJSONArray("actions")));
						flow.setMatch(MatchJSON.getMatch(obj.getJSONObject("match")));
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
						flow.setDurationSeconds(String.valueOf(obj
									.getInt("durationSeconds")));
						flow.setPacketCount(String.valueOf(obj.getInt("packetCount")));
						flow.setByteCount(FormatLong.format(obj.getLong("byteCount")));
						flows.add(flow);
			}
		}
		return flows;
	}
}
