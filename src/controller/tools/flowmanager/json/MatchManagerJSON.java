package controller.tools.flowmanager.json;

import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import model.tools.flowmanager.Action;
import model.tools.flowmanager.Match;

import org.openflow.util.HexString;

import view.Gui;

import avior.json.JSONArray;
import avior.json.JSONException;
import avior.json.JSONObject;
import controller.Deserializer;

public class MatchManagerJSON {

	String dataLayerDestination, dataLayerSource, dataLayerType, dataLayerVLAN,
	dataLayerPCP, inputPort, networkDestination,
	networkDestinationMaskLength, networkProtocol, networkSource,
	networkSourceMaskLength, networkTypeOfService,
	transportDestination, transportSource, wildcards;
	
	private static String IP = Gui.IP;
	static JSONObject obj;
	static JSONArray json;
	
	
	public static Match getMatch(String dpid, String flowName) throws JSONException, IOException {
		Match match = new Match();
		// Get the match object
		obj = Deserializer.readJsonObjectFromURL("http://" + IP
				+ ":8080/wm/staticflowentrypusher/list/" + dpid + "/json");
		obj = obj.getJSONObject(dpid).getJSONObject(flowName).getJSONObject("match");
		
		// Here we check the values, if they are default we set them to null. This way they don't confuse the user into thinking they set something they didn't
		if(!obj.getString("dataLayerDestination").equals("00:00:00:00:00:00"))
			match.setDataLayerDestination(obj.getString("dataLayerDestination"));
		if(!obj.getString("dataLayerSource").equals("00:00:00:00:00:00"))
			match.setDataLayerSource(obj.getString("dataLayerSource"));
		if(!obj.getString("dataLayerType").equals("0x0000"))
			match.setDataLayerType(obj.getString("dataLayerType"));
		if(obj.getInt("dataLayerVirtualLan") != -1)
			match.setDataLayerVLAN(String.valueOf(obj.getInt("dataLayerVirtualLan")));
		if(obj.getInt("dataLayerVirtualLanPriorityCodePoint") != 0)
			match.setDataLayerPCP(String.valueOf(obj.getInt("dataLayerVirtualLanPriorityCodePoint")));
		if(obj.getInt("inputPort") != 0)
			match.setInputPort(String.valueOf(obj.getInt("inputPort")));
		if(!obj.getString("networkDestination").equals("0.0.0.0"))
			match.setNetworkDestination(obj.getString("networkDestination"));
		//match.setNetworkDestinationMaskLength(String.valueOf(obj.getInt("networkDestinationMaskLen")));
		if(obj.getInt("networkProtocol") != 0)
			match.setNetworkProtocol(String.valueOf(obj.getInt("networkProtocol")));
		if(!obj.getString("networkSource").equals("0.0.0.0"))
			match.setNetworkSource(obj.getString("networkSource"));
		//match.setNetworkSourceMaskLength(String.valueOf(obj.getInt("networkSourceMaskLen")));
		if(obj.getInt("networkTypeOfService") != 0)
			match.setNetworkTypeOfService(String.valueOf(obj.getInt("networkTypeOfService")));
		if(obj.getInt("transportDestination") != 0)
			match.setTransportDestination(String.valueOf(obj.getInt("transportDestination")));
		if(obj.getInt("transportSource") != 0)
			match.setTransportSource(String.valueOf(obj.getInt("transportSource")));
			match.setWildcards(String.valueOf(obj.getLong("wildcards")));
		
		return match;
	}
}
