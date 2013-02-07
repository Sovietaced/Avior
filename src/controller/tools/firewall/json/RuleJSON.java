package controller.tools.firewall.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import model.tools.firewall.Rule;

import view.Gui;
import controller.util.Deserializer;
import controller.util.JSONArray;
import controller.util.JSONException;
import controller.util.JSONObject;

public class RuleJSON {

	static String IP = Gui.IP;
	static JSONObject obj;
	static JSONArray json;
	static Future<Object> future;

	public static List<Rule> getRules()
			throws JSONException, IOException {

		List<Rule> rules = new ArrayList<Rule>();
		// Get the array of actions
		future = Deserializer.readJsonArrayFromURL("http://" + IP
				+ ":8080/wm/firewall/rules/json");
		try {
			json = (JSONArray) future.get(5, TimeUnit.SECONDS);
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
		
			for (int i = 0; i < json.length(); i++) {
				obj = json.getJSONObject(i);
				Rule rule = new Rule();
				rule.setRuleid(obj.getInt("ruleid"));
				rule.setDpid(obj.getString("dpid"));
				rule.setIn_port(String.valueOf((obj.getInt("in_port"))));
				rule.setDl_src(obj.getString("dl_src"));
				rule.setDl_dst(obj.getString("dl_dst"));
				rule.setDl_type(String.valueOf((short)(obj.getInt("dl_type"))));
				rule.setNw_src_prefix(obj.getString("nw_src_prefix"));
				rule.setNw_src_maskbits(String.valueOf(obj.getInt("nw_src_maskbits")));
				rule.setNw_dst_prefix(obj.getString("nw_dst_prefix"));
				rule.setNw_dst_maskbits(String.valueOf(obj.getInt("nw_dst_maskbits")));
				rule.setNw_proto(String.valueOf((short)(obj.getInt("nw_proto"))));
				rule.setTp_src(String.valueOf((short)(obj.getInt("tp_src"))));
				rule.setTp_dst(String.valueOf((short)(obj.getInt("tp_dst"))));
				rule.setWildcard_dpid(obj.getBoolean("wildcard_dpid"));
				rule.setWildcard_in_port(obj.getBoolean("wildcard_in_port"));
				rule.setWildcard_dl_src(obj.getBoolean("wildcard_dl_src"));
				rule.setWildcard_dl_dst(obj.getBoolean("wildcard_dl_dst"));
				rule.setWildcard_dl_type(obj.getBoolean("wildcard_dl_type"));
				rule.setWildcard_nw_src(obj.getBoolean("wildcard_nw_src"));
				rule.setWildcard_nw_dst(obj.getBoolean("wildcard_nw_dst"));
				rule.setWildcard_nw_proto(obj.getBoolean("wildcard_nw_proto"));
				rule.setWildcard_tp_src(obj.getBoolean("wildcard_tp_src"));
				rule.setWildcard_tp_dst(obj.getBoolean("wildcard_tp_dst"));
				rule.setPriority(String.valueOf(obj.getInt("priority")));
				rule.setAction(obj.getString("action"));			
				rules.add(rule);
		}
		return rules;
	}

}
