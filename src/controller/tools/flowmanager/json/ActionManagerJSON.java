package controller.tools.flowmanager.json;

import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import model.tools.flowmanager.Action;

import view.Gui;
import controller.util.Deserializer;
import controller.util.HexString;
import controller.util.JSONArray;
import controller.util.JSONException;
import controller.util.JSONObject;

public class ActionManagerJSON {

	static String IP = Gui.IP;
	static JSONObject obj;
	static JSONArray json;

	// This parses JSON from the restAPI to get all the actions and values for that action by it's flow name
	public static List<Action> getActions(String dpid, String flowName)
			throws JSONException, IOException {

		List<Action> actions = new ArrayList<Action>();
		// Get the array of actions
		obj = Deserializer.readJsonObjectFromURL("http://" + IP
				+ ":8080/wm/staticflowentrypusher/list/" + dpid + "/json");
		if (!obj.getJSONObject(dpid).getJSONObject(flowName).isNull("actions")) {
			json = obj.getJSONObject(dpid).getJSONObject(flowName)
					.getJSONArray("actions");

			for (int i = 0; i < json.length(); i++) {
				obj = json.getJSONObject(i);
				String objActionType = obj.getString("type");
				if (objActionType.equals("OUTPUT")) {
					actions.add(new Action("output", String.valueOf(obj
							.getInt("port")), "Port"));
				} else if (objActionType.equals("OPAQUE_ENQUEUE")) {
					actions.add(new Action("enqueue", String.valueOf(obj
							.getInt("port") + ":" + obj.getInt("queueId")),
							"Port:Queue ID"));
				} else if (objActionType.equals("STRIP_VLAN")) {
					actions.add(new Action("strip-vlan", ""));
				} else if (objActionType.equals("SET_VLAN_ID")) {
					actions.add(new Action("set-vlan-id", String.valueOf(obj
							.getInt("virtualLanIdentifier")), "VLAN ID"));
				} else if (objActionType.equals("SET_VLAN_PCP")) {
					actions.add(new Action("set-vlan-priority",
							String.valueOf(obj
									.getInt("virtualLanPriorityCodePoint")),
							"VLAN PCP"));
				} else if (objActionType.equals("SET_DL_SRC")) {
					String dl = obj.getString("dataLayerAddress");
					actions.add(new Action("set-src-mac", String
							.valueOf(HexString.toHexString(dl.getBytes())),
							"Data Layer Address"));
				} else if (objActionType.equals("SET_DL_DST")) {
					String dl = obj.getString("dataLayerAddress");
					actions.add(new Action("set-dst-mac", String
							.valueOf(HexString.toHexString(dl.getBytes())),
							"Data Layer Address"));
				} else if (objActionType.equals("SET_NW_TOS")) {
					actions.add(new Action("set-tos-bits", String.valueOf(obj
							.getInt("networkTypeOfService")),
							"Network Type Of Service"));
				} else if (objActionType.equals("SET_NW_SRC")) {
					long ip = obj.getLong("networkAddress");
					byte[] bytes = BigInteger.valueOf(ip).toByteArray();
					InetAddress address = null;

					try {
						address = InetAddress.getByAddress(bytes);
					} catch (UnknownHostException e) {
						System.out.println("Getting address failed.");
						e.printStackTrace();
					}

					actions.add(new Action("set-src-ip", address.toString()
							.replaceAll("/", ""), "Network Address"));
				} else if (objActionType.equals("SET_NW_DST")) {
					long ip = obj.getLong("networkAddress");
					byte[] bytes = BigInteger.valueOf(ip).toByteArray();
					InetAddress address = null;

					try {
						address = InetAddress.getByAddress(bytes);
					} catch (UnknownHostException e) {
						System.out.println("Getting address failed.");
						e.printStackTrace();
					}

					actions.add(new Action("set-dst-ip", address.toString()
							.replaceAll("/", ""), "Network Address"));
				} else if (objActionType.equals("SET_TP_SRC")) {
					actions.add(new Action("set-src-port", String.valueOf(obj
							.getInt("transportPort")), "Transport Port"));
				} else if (objActionType.equals("SET_TP_DST")) {
					actions.add(new Action("set-dst-port", String.valueOf(obj
							.getInt("transportPort")), "Transport Port"));
				}
			}
		}
		return actions;
	}

}
