package controller.overview.json;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import controller.floodlightprovider.FloodlightProvider;
import controller.util.Deserializer;
import controller.util.JSONArray;
import controller.util.JSONException;
import controller.util.JSONObject;

import model.overview.DeviceSummary;

public class DevicesJSON {

	private static String IP = FloodlightProvider.getIP();
	private static String PORT = FloodlightProvider.getPort();
	private static JSONObject obj;

	public static List<DeviceSummary> getDeviceSummaries() throws JSONException {

		List<DeviceSummary> deviceSummaries = new ArrayList<DeviceSummary>();

		// Get the string IDs of all the switches and create switch summary
		// objects for each one
		try {
			Future<Object> devices = Deserializer.readJsonArrayFromURL("http://" + IP
					+ ":" + PORT + "/wm/device/");
			JSONArray json = (JSONArray) devices.get(5, TimeUnit.SECONDS);
			for (int i = 0; i < json.length(); i++) {
				obj = json.getJSONObject(i);
				DeviceSummary temp = new DeviceSummary(obj.getJSONArray("mac")
						.getString(0));
				if (!obj.getJSONArray("ipv4").isNull(0))
					temp.setIpv4(obj.getJSONArray("ipv4").getString(0));
				if (!obj.getJSONArray("attachmentPoint").isNull(0)) {
					temp.setAttachedSwitch(obj.getJSONArray("attachmentPoint")
							.getJSONObject(0).getString("switchDPID"));
					temp.setSwitchPort(obj.getJSONArray("attachmentPoint")
							.getJSONObject(0).getInt("port"));
				}
				Date d = new Date(obj.getLong("lastSeen"));
				temp.setLastSeen(d);
				deviceSummaries.add(temp);
			}
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
		return deviceSummaries;
	}
}
