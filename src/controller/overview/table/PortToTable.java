package controller.overview.table;

import java.util.ArrayList;
import java.util.List;

import model.overview.Port;

public class PortToTable {

	// This returns a table representation of a list of ports on a switch
	public static String[][] getPortTableFormat(List<Port> ports) {
		
		if(!ports.isEmpty()){
			int count = 0;
			String[][] arrData = new String[ports.size()][8];
		
		for(Port port : ports){
			List<String> stringList = new ArrayList<String>();
			stringList.add(port.getPortNumber());
			stringList.add(port.getStatus());
			stringList.add(port.getTransmitBytes());
			stringList.add(port.getReceiveBytes());
			stringList.add(port.getTransmitPackets());
			stringList.add(port.getReceivePackets());
			stringList.add(String.valueOf(Integer.valueOf(port.getTransmitDropped()) + Integer.valueOf(port.getReceiveDropped())));
			stringList.add(port.getErrors());
			arrData[count] = stringList.toArray(new String[stringList.size()]);
			count++;
		}
		return arrData;
		}
		else
			return new String[0][0];
	}
}