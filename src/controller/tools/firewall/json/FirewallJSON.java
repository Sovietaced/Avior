package controller.tools.firewall.json;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import controller.floodlightprovider.FloodlightProvider;
import controller.util.Deserializer;
import controller.util.JSONException;
import controller.util.JSONObject;

public class FirewallJSON {

	private static String IP = FloodlightProvider.getIP();
	private static String PORT = FloodlightProvider.getPort();
	private static JSONObject obj;
	private static Future<Object> future;

	public static boolean isEnabled()
			throws JSONException, IOException {

		future = Deserializer.readJsonObjectFromURL("http://" + IP
				+ ":" + PORT + "/wm/firewall/module/status/json");
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
			if(obj.getString("result").equals("firewall enabled"))
				return true;
		}
		return false;
	}
	
	public static String enable(boolean enable) throws JSONException{
		
		if(enable){
			future = Deserializer.readJsonObjectFromURL("http://" + IP
					+ ":" + PORT + "/wm/firewall/module/enable/json");
			
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
				if(obj.getString("status").equals("success"))
					return obj.getString("status");
				else
					return null;
			}
		}
		else{
			future = Deserializer.readJsonObjectFromURL("http://" + IP
					+ ":" + PORT + "/wm/firewall/module/disable/json");
			
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
				if(obj.getString("status").equals("success"))
					return obj.getString("status");
				else
					return null;
			}
		}
		return null;
	}
}
