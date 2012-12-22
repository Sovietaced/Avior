package controller.tools.firewall.json;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


import view.Gui;
import controller.util.Deserializer;
import controller.util.JSONArray;
import controller.util.JSONException;
import controller.util.JSONObject;

public class FirewallJSON {

	static String IP = Gui.IP;
	static JSONObject obj;
	static JSONArray json;
	static Future<Object> future;

	public static boolean isEnabled()
			throws JSONException, IOException {

		future = Deserializer.readJsonObjectFromURL("http://" + IP
				+ ":8080/wm/firewall/module/status/json");
		try {
			obj = (JSONObject) future.get(5, TimeUnit.SECONDS);
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
		if (obj != null) {
			if(obj.getString("result") == "firewall enabled")
				return true;
		}
		return false;
	}
	
	public static boolean enable(boolean enable) throws JSONException{
		
		if(enable){
			future = Deserializer.readJsonObjectFromURL("http://" + IP
					+ ":8080/wm/firewall/module/enable/json");
			
			try {
				obj = (JSONObject) future.get(5, TimeUnit.SECONDS);
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
			if (obj != null) {
				if(obj.getString("status") == "success")
					return true;
				else
					return false;
			}
		}
		else{
			future = Deserializer.readJsonObjectFromURL("http://" + IP
					+ ":8080/wm/firewall/module/disable/json");
			
			try {
				obj = (JSONObject) future.get(5, TimeUnit.SECONDS);
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
			if (obj != null) {
				if(obj.getString("status") == "success")
					return true;
				else
					return false;
			}
		}
		//default return
		return false;
	}
}
