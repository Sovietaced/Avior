package controller.overview.switches.json;

import java.io.IOException;

import model.tools.flowmanager.Match;

import view.Gui;

import controller.util.Deserializer;
import controller.util.JSONArray;
import controller.util.JSONException;
import controller.util.JSONObject;

public class MatchJSON {

	String dataLayerDestination, dataLayerSource, dataLayerType, dataLayerVLAN,
			dataLayerPCP, inputPort, networkDestination,
			networkDestinationMaskLength, networkProtocol, networkSource,
			networkSourceMaskLength, networkTypeOfService,
			transportDestination, transportSource, wildcards;

	static JSONObject obj;
	static JSONArray json;

	public static Match getMatch(JSONObject obj)
			throws JSONException, IOException {
		
		Match match = new Match();
		
		// Here we check the values, if they are default we set them to null.
		// This way they don't confuse the user into thinking they set something
		// they didn't
		if (!obj.getString("dataLayerDestination").equals("00:00:00:00:00:00"))
			match.setDataLayerDestination(obj.getString("dataLayerDestination"));
		if (!obj.getString("dataLayerSource").equals("00:00:00:00:00:00"))
			match.setDataLayerSource(obj.getString("dataLayerSource"));
		if (!obj.getString("dataLayerType").equals("0x0000"))
			match.setDataLayerType(obj.getString("dataLayerType"));
		if (obj.getInt("dataLayerVirtualLan") != -1)
			match.setDataLayerVLAN(String.valueOf(obj
					.getInt("dataLayerVirtualLan")));
		if (obj.getInt("dataLayerVirtualLanPriorityCodePoint") != 0)
			match.setDataLayerPCP(String.valueOf(obj
					.getInt("dataLayerVirtualLanPriorityCodePoint")));
		if (obj.getInt("inputPort") != 0)
			match.setInputPort(String.valueOf(obj.getInt("inputPort")));
		if (!obj.getString("networkDestination").equals("0.0.0.0"))
			match.setNetworkDestination(obj.getString("networkDestination"));
		// match.setNetworkDestinationMaskLength(String.valueOf(obj.getInt("networkDestinationMaskLen")));
		if (obj.getInt("networkProtocol") != 0)
			match.setNetworkProtocol(String.valueOf(obj
					.getInt("networkProtocol")));
		if (!obj.getString("networkSource").equals("0.0.0.0"))
			match.setNetworkSource(obj.getString("networkSource"));
		// match.setNetworkSourceMaskLength(String.valueOf(obj.getInt("networkSourceMaskLen")));
		if (obj.getInt("networkTypeOfService") != 0)
			match.setNetworkTypeOfService(String.valueOf(obj
					.getInt("networkTypeOfService")));
		if (obj.getInt("transportDestination") != 0)
			match.setTransportDestination(String.valueOf(obj
					.getInt("transportDestination")));
		if (obj.getInt("transportSource") != 0)
			match.setTransportSource(String.valueOf(obj
					.getInt("transportSource")));
		match.setWildcards(String.valueOf(obj.getLong("wildcards")));

		return match;
	}
}
