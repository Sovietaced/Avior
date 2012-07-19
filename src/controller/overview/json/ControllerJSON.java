package controller.overview.json;

import java.io.IOException;
import java.util.Iterator;

import controller.util.Deserializer;
import controller.util.JSONException;
import controller.util.JSONObject;

import view.Gui;


public class ControllerJSON {

	static String IP = Gui.IP;
	static JSONObject obj;

	public static String[] getControllerInfo() throws JSONException {

		String[] info = new String[4];

		// Add the ip address of the controller
		info[0] = (IP);

		// Get whether the controller is healthy or not
		try {
			obj = Deserializer.readJsonObjectFromURL("http://" + IP
					+ ":8080/wm/core/health/json");
			if (obj.getBoolean("healthy")) {
				info[1] = "Yes";
			} else {
				info[1] = "No";
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed to get read JSON Object");
		}

		// Get the packets/bytes/flows information for each switch and add it to
		// the summary
		try {
			obj = Deserializer.readJsonObjectFromURL("http://" + IP
					+ ":8080/wm/core/memory/json");
			long free = obj.getLong("free");
			long total = obj.getLong("total");
			info[2] = (formatLong(free) + " free of " + formatLong(total));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed to get read JSON Object");
		}

		// Get the packets/bytes/flows information for each switch and add it to
		// the summary
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
			info[3] = modules;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed to get read JSON Object");
		}

		return info;
	}

	/* Formats longs for proper representation in the paint */
	public static String formatLong(long paramLong) {
		if (paramLong >= 1000000000L) {
			return paramLong / 1000000000L + "GB";
		}
		if (paramLong >= 10000000L) {
			return paramLong / 1000000L + "MB";
		}
		if (paramLong >= 1000L) {
			return paramLong / 1000L + "KB";
		}
		return "" + paramLong;
	}
}
