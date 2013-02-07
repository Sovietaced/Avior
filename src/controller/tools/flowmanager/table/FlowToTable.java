package controller.tools.flowmanager.table;

import model.tools.flowmanager.Flow;

public class FlowToTable {

	// This returns a table representation of the specified flow
	public static String[][] getFlowTableFormat(Flow flow) {

		String[][] f = { { "Name", flow.getName() }, { "Actions", "..." },
				{ "Match", "..." },
				{ "Priority", flow.getPriority() }};

		return f;
	}

	// This returns a table representation of a new flow
	public static String[][] getNewFlowTableFormat() {

		String[][] f = { { "Name", }, { "Actions", "..." }, { "Match", "..." },
				{ "Priority" }};

		return f;
	}
}
