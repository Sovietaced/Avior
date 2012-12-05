package controller.overview.switches.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
	static Future<Object> future;

	// This parses JSON from the restAPI to get all the flows from a specified switch, meant for the controller overview
	public static List<Flow> getFlows(JSONObject obj, String dpid) throws IOException,
			JSONException {

		List<Flow> flows = new ArrayList<Flow>();
		
		// If JSONObject is not supplied, get it.
		if(obj == null){
			try {
				obj = (JSONObject) Deserializer.readJsonObjectFromURL("http://" + IP
						+ ":8080/wm/core/switch/" + dpid + "/flow/json").get(5, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (!obj.isNull(dpid)) {
			json = obj.getJSONArray(dpid);
			for (int i = 0; i < json.length(); i++) {
				obj = (JSONObject) json.get(i);
				Flow flow = new Flow(dpid);
				flow.setActions(ActionJSON.getActions(obj
						.getJSONArray("actions")));
				flow.setMatch(MatchJSON.getMatch(obj.getJSONObject("match")));
				flow.setPriority(String.valueOf(obj.getInt("priority")));
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
				flow.setByteCount(FormatLong.formatBytes(obj.getLong("byteCount"),false,false));
				flows.add(flow);
			}
		}
		return flows;
	}
}
