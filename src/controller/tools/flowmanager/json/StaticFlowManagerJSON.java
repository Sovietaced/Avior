package controller.tools.flowmanager.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import model.tools.flowmanager.Flow;

import controller.floodlightprovider.FloodlightProvider;
import controller.util.Deserializer;
import controller.util.JSONArray;
import controller.util.JSONException;
import controller.util.JSONObject;

public class StaticFlowManagerJSON {

	static String IP = FloodlightProvider.getIP();
	static JSONObject jsonobj, obj;
	static JSONArray json;
	static Future<Object> future;

	// This parses JSON from the restAPI to get all the flows from a switch
	public static List<Flow> getFlows(String sw) throws IOException,
			JSONException {
		
		List<Flow> flows = new ArrayList<Flow>();
		
		// Get the string names of all the specified switch's flows
		future = Deserializer.readJsonObjectFromURL("http://" + IP
				+ ":8080/wm/staticflowentrypusher/list/" + sw + "/json");
		try {
			jsonobj = (JSONObject) future.get(5, TimeUnit.SECONDS);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (TimeoutException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
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
						flow.setPriority(String.valueOf(obj
									.getInt("priority")));
						flows.add(flow);
						System.out.println(flow.serialize());
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
