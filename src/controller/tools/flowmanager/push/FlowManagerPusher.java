package controller.tools.flowmanager.push;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.eclipse.swt.widgets.TableItem;

import controller.overview.switches.json.FlowJSON;
import controller.util.JSONException;
import controller.util.JSONObject;

import model.tools.flowmanager.Flow;

import view.Gui;
import view.tools.flowmanager.StaticFlowManager;

public class FlowManagerPusher {

	static String IP = Gui.IP;

	public static String push(Flow flow) throws IOException, JSONException {

		String warning = "Warning! Pushing a static flow entry that matches IP " +
                "fields without matching for IP payload (ether-type 2048) will cause " +
                "the switch to wildcard higher level fields.";
                
		String jsonResponse = "";
		URL url = new URL("http://" + IP
				+ ":8080/wm/staticflowentrypusher/json");
		URLConnection conn = url.openConnection();
		conn.setDoOutput(true);
		OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		wr.write(flow.serialize());
		wr.flush();

		// Get the response
		BufferedReader rd = new BufferedReader(new InputStreamReader(
				conn.getInputStream()));
		String line;
		while ((line = rd.readLine()) != null) {
			jsonResponse = jsonResponse.concat(line);
		}
		wr.close();
		rd.close();

		// Wrap the response
		JSONObject json = new JSONObject(jsonResponse);
		
		// Make sure the static flow pusher throws no errors
		if (json.getString("status").equals("Entry pushed") || json.getString("status").equals(warning)) {
			List<Flow> actualFlows = FlowJSON.getFlows(flow.getSwitch());
			// Compare the flow you just pushed with those actually on the switch
			// If found, success message printed.
			for (Flow actualFlow : actualFlows) {
				if (flow.equals(actualFlow)) {
					return "Flow successfully pushed!";
				}
			}
			// If not found give user guidance
			return "Flow was dropped by the switch. Check your controller log for more details.";
		} else {
			return json.getString("status");
		}
	}

	public static String remove(Flow flow) throws IOException, JSONException {

		String jsonResponse = "";

		URL url = new URL("http://" + IP
				+ ":8080/wm/staticflowentrypusher/json");
		HttpURLConnection connection = null;
		connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		// We have to override the post method so we can send data
		connection.setRequestProperty("X-HTTP-Method-Override", "DELETE");
		connection.setDoOutput(true);

		// Send request
		OutputStreamWriter wr = new OutputStreamWriter(
				connection.getOutputStream());
		wr.write(flow.deleteString());
		wr.flush();

		// Get Response
		BufferedReader rd = new BufferedReader(new InputStreamReader(
				connection.getInputStream()));
		String line;
		while ((line = rd.readLine()) != null) {
			jsonResponse = jsonResponse.concat(line);
		}
		wr.close();
		rd.close();

		JSONObject json = new JSONObject(jsonResponse);
		// Return result string from key "status"
		return json.getString("status");
	}

	public static void deleteAll(String dpid) throws IOException, JSONException {
		// This makes a simple get request that will delete all flows from a
		// switch
		String urlString = "http://" + IP
				+ ":8080/wm/staticflowentrypusher/clear/" + dpid + "/json";
		URL url = new URL(urlString);
		URLConnection conn = url.openConnection();
		InputStream is = conn.getInputStream();
	}

	public static Flow parseTableChanges(TableItem[] items) {
		Flow flow = StaticFlowManager.getFlow();
		if (!items[0].getText(1).isEmpty())
			flow.setName(items[0].getText(1));
		if (!items[3].getText(1).isEmpty())
			flow.setPriority(items[3].getText(1));
		if (!items[4].getText(1).isEmpty())
			flow.setCookie(items[4].getText(1));
		if (!items[5].getText(1).isEmpty())
			flow.setIdleTimeOut(items[5].getText(1));
		if (!items[6].getText(1).isEmpty())
			flow.setHardTimeOut(items[6].getText(1));
		if (!items[7].getText(1).isEmpty())
			flow.setOutPort(items[7].getText(1));
		return flow;
	}
}
