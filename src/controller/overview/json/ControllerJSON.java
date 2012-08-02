package controller.overview.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import controller.util.Deserializer;
import controller.util.FormatLong;
import controller.util.JSONException;
import controller.util.JSONObject;

import view.Gui;


public class ControllerJSON {

	static String IP = Gui.IP;
	static JSONObject obj;

	public static List<String> getControllerInfo() throws JSONException {

		List <String> info = new ArrayList<String>();

		// Add the ip address of the controller
		info.add(0,IP);

		// Get whether the controller is healthy or not
		try {
			obj = Deserializer.readJsonObjectFromURL("http://" + IP
					+ ":8080/wm/core/health/json");
			if (obj.getBoolean("healthy")) {
				info.add(1,"Yes");
			} else {
				info.add(1,"No");
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed to get read JSON Object");
		}

		// Get the JVM memory bloat
		try {
			obj = Deserializer.readJsonObjectFromURL("http://" + IP
					+ ":8080/wm/core/memory/json");
			long free = obj.getLong("free");
			long total = obj.getLong("total");
			info.add(2,FormatLong.formatBytes(free,true,false) + " free of " + FormatLong.formatBytes(total,true,false));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed to get read JSON Object");
		}

		// Get the modules loaded for the controller
		try {
			obj = Deserializer.readJsonObjectFromURL("http://" + IP
					+ ":8080/wm/core/module/loaded/json");
			Iterator<?> myIter = obj.keys();
			String modules = "";
			while (myIter.hasNext()) {
				try {
					String key = (String) myIter.next();
					if (obj.get(key) instanceof JSONObject) {
						modules = modules.concat(key + " ");
					}
				} catch (Exception e) {
					System.out.println("swag sack");
				}
			}
			info.add(modules);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed to get read JSON Object");
		}

		return info;
	}
}
