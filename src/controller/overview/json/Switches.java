package controller.overview.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import controller.Deserializer;

import model.overview.SwitchSummary;

import avior.json.JSONArray;
import avior.json.JSONException;
import avior.json.JSONObject;

import view.Gui;

public class Switches {
	
	static String IP = Gui.IP;
	static JSONObject obj;
	
	public static List<SwitchSummary> getSwitchSummaries() throws JSONException{
			
		List<SwitchSummary> switchSummaries = new ArrayList<SwitchSummary>();
		
			// Get the string IDs of all the switches and create switch summary objects for each one
			try {
				JSONArray json = Deserializer.readJsonArrayFromURL("http://" + IP + ":8080/wm/core/controller/switches/json");
				for(int i = 0; i < json.length(); i++){
					obj = json.getJSONObject(i);
					switchSummaries.add(new SwitchSummary(obj.getString("dpid")));
				}
			} catch (IOException e) {
			System.out.println("Fail sauce!");
			}
			
			 //Get the vendor information for each switch and add it to the summary
			try {
				for(SwitchSummary sum : switchSummaries){
					obj = Deserializer.readJsonObjectFromURL("http://" + IP + ":8080/wm/core/switch/" + sum.getDpid() + "/desc/json");
					JSONObject desc = obj.getJSONArray(sum.getDpid()).getJSONObject(0);
					sum.setVendor(desc.getString("manufacturerDescription"));
				}
			} catch (IOException e) {
				System.out.println("Fail sauce!");
			}
			
			 //Get the packets/bytes/flows information for each switch and add it to the summary
			try {
				for(SwitchSummary sum : switchSummaries){
					obj = Deserializer.readJsonObjectFromURL("http://" + IP + ":8080/wm/core/switch/" + sum.getDpid() + "/aggregate/json");
					JSONObject aggr = obj.getJSONArray(sum.getDpid()).getJSONObject(0);
					sum.setPacketCount(aggr.getInt("packetCount"));
					sum.setByteCount(aggr.getInt("byteCount"));
					sum.setFlowCount(aggr.getInt("flowCount"));
				}
			} catch (IOException e) {
				System.out.println("Fail sauce!");
			}
			
			return switchSummaries;
	}

	public static String[][] switchSummariesToTable() {
		
		List<SwitchSummary> summaries = null;
		try {
			summaries = getSwitchSummaries();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		String[][] tableArr = new String[summaries.size()][6];
		int count = 0;
		
		for(SwitchSummary sum : summaries){
			List<String> stringList = new ArrayList<String>();
			stringList.add(String.valueOf(count+1));
			stringList.add(sum.getDpid());
			stringList.add(sum.getManufacturer());
			stringList.add(String.valueOf(sum.getPacketCount()));
			stringList.add(String.valueOf(sum.getByteCount()));
			stringList.add(String.valueOf(sum.getFlowCount()));
			
			tableArr[count] = stringList.toArray(new String[stringList.size()]);
			count++;
		}
		return tableArr;
	}
}
