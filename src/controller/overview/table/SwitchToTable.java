package controller.overview.table;

import java.util.ArrayList;
import java.util.List;

import model.overview.Switch;

public class SwitchToTable {

	// This returns a table representation of a switch from a list of switches
	public static String[][] getSwitchTableFormat(List<Switch> switches) {
		
		if(switches != null){
			int count = 0;
			String[][] arrData = new String[switches.size()][5];
		
		for(Switch sw : switches){
			List<String> stringList = new ArrayList<String>();
			stringList.add(String.valueOf(count + 1));
			stringList.add(sw.getDpid());
			stringList.add(sw.getManufacturerDescription());
			stringList.add(sw.getPacketCount());
			stringList.add(sw.getByteCount());
			stringList.add(sw.getFlowCount());
			arrData[count] = stringList.toArray(new String[stringList.size()]);
			count++;
		}
		return arrData;
		}
		else
			return new String[0][0];
	}
}