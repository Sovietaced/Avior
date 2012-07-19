package controller.overview.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import controller.util.Deserializer;
import controller.util.JSONArray;
import controller.util.JSONException;
import controller.util.JSONObject;

import model.overview.DeviceSummary;

import view.Gui;

public class DevicesJSON {

	static String IP = Gui.IP;
	static JSONObject obj;

	public static List<DeviceSummary> getDeviceSummaries() throws JSONException {

		List<DeviceSummary> deviceSummaries = new ArrayList<DeviceSummary>();

		// Get the string IDs of all the switches and create switch summary
		// objects for each one
		try {
			JSONArray json = Deserializer.readJsonArrayFromURL("http://" + IP
					+ ":8080/wm/device/");
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
		} catch (IOException e) {
			System.out.println("Fail sauce!");
		}
		return deviceSummaries;
	}

	public static String[][] deviceSummariesToTable() {

		List<DeviceSummary> summaries = null;
		try {
			summaries = getDeviceSummaries();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String[][] tableArr = new String[summaries.size()][6];
		int count = 0;

		for (DeviceSummary sum : summaries) {
			List<String> stringList = new ArrayList<String>();
			stringList.add(String.valueOf(count + 1));
			stringList.add(sum.getMacAddress());
			if (sum.getIpv4() != null)
				stringList.add(sum.getIpv4());
			else
				stringList.add("None");
			if (sum.getAttachedSwitch() != null)
				stringList.add(sum.getAttachedSwitch());
			else
				stringList.add("None");
			if (sum.getSwitchPort() != 0)
				stringList.add(String.valueOf(sum.getSwitchPort()));
			else
				stringList.add("None");
			stringList.add(String.valueOf(sum.getLastSeen()));

			tableArr[count] = stringList.toArray(new String[stringList.size()]);
			count++;
		}
		return tableArr;
	}
}
