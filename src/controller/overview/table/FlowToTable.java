package controller.overview.table;

import java.util.ArrayList;
import java.util.List;

import model.tools.flowmanager.Flow;

public class FlowToTable {

	// This returns a table representation of a flow, formatted for the controller overview
	public static String[][] getFlowTableFormat(List<Flow> flows) {

		if (flows != null) {
			int count = 0;
			String[][] arrData = new String[flows.size()][8];

			for (Flow flow : flows) {
				List<String> stringList = new ArrayList<String>();
				stringList.add(String.valueOf(count + 1));
				stringList.add(flow.getPriority());
				stringList.add(flow.getMatch().toString());
				stringList.add(flow.actionsToString());
				stringList.add(flow.getPacketCount());
				stringList.add(flow.getByteCount());
				stringList.add(flow.getDurationSeconds());
				if (flow.getIdleTimeOut() != null) {
					stringList.add(flow.getIdleTimeOut());
				} else {
					stringList.add("Static");
				}
				arrData[count] = stringList.toArray(new String[stringList
						.size()]);
				count++;
			}
			return arrData;
		} else
			return new String[0][0];
	}
}