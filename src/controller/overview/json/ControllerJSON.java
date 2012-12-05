package controller.overview.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import controller.util.Deserializer;
import controller.util.FormatLong;
import controller.util.JSONException;
import controller.util.JSONObject;

import view.Gui;


public class ControllerJSON {

	static String IP = Gui.IP;
	static JSONObject obj;
	static Future<Object> futureHealth,futureModules,futureMemory;

	public static List<String> getControllerInfo() throws JSONException, IOException {

		List <String> info = new ArrayList<String>();

		// Add the ip address of the controller
		info.add(0,IP);

		// Start threads that make calls to the restAPI
		futureHealth = Deserializer.readJsonObjectFromURL("http://" + IP
				+ ":8080/wm/core/health/json");
		futureMemory = Deserializer.readJsonObjectFromURL("http://" + IP
				+ ":8080/wm/core/memory/json");
		futureModules = Deserializer.readJsonObjectFromURL("http://" + IP
				+ ":8080/wm/core/module/loaded/json");
		
		// HEALTH
		try {
			obj = (JSONObject) futureHealth.get(5, TimeUnit.SECONDS);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (ExecutionException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (TimeoutException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		if (obj.getBoolean("healthy")) {
			info.add(1,"Yes");
		} else {
			info.add(1,"No");
		}

		// MEMORY
		try {
			obj = (JSONObject) futureMemory.get(5, TimeUnit.SECONDS);
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
		long free = obj.getLong("free");
		long total = obj.getLong("total");
		info.add(2,FormatLong.formatBytes(free,true,false) + " free of " + FormatLong.formatBytes(total,true,false));

		// MODULES
		try {
			obj = (JSONObject) futureModules.get(5, TimeUnit.SECONDS);
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
		Iterator<?> myIter = obj.keys();
		String modules = "";
		while (myIter.hasNext()) {
			try {
				String key = (String) myIter.next();
				if (obj.get(key) instanceof JSONObject) {
					modules = modules.concat(key + " ");
				}
			} catch (Exception e) {
				// Fail silently
			}
		}
		info.add(modules);

		return info;
	}
}
