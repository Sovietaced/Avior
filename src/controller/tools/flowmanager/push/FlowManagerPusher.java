package controller.tools.flowmanager.push;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import org.eclipse.swt.widgets.TableItem;

import avior.json.JSONException;
import avior.json.JSONObject;

import model.tools.flowmanager.Flow;

import view.Gui;
import view.tools.flowmanager.FlowManager;

public class FlowManagerPusher {

	static String IP = Gui.IP;

	public static String push(Flow flow) throws IOException, JSONException {

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

		JSONObject json = new JSONObject(jsonResponse);
		// Return result string from key "status"
		return json.getString("status");
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

	public static void removeAll(String dpid) throws IOException, JSONException {
		URL url = new URL("http://" + IP
				+ ":8080/wm/staticflowentrypusher/clear/" + dpid + "/json");
		URLConnection conn = url.openConnection();
		conn.setDoOutput(true);
	}

	public static Flow parseTableChanges(TableItem[] items) {

		Flow flow = FlowManager.getFlow();
		if (!items[0].getText(1).isEmpty())
			flow.setName(items[0].getText(1));
		if (!items[3].getText(1).isEmpty())
			flow.setPriority(items[0].getText(1));
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
