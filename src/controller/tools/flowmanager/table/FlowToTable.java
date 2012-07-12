package controller.tools.flowmanager.table;

import model.tools.flowmanager.Flow;

public class FlowToTable {

	// Gets an action by it's index and formats it so that it can be displayed in a table
	public static String[][] getFlowTableFormat(Flow flow){
			
		String[][] f = { { "Name", flow.getName()},
				{ "Actions", "..." },
				{ "Match", "..." },
				{ "Priority", flow.getPriority() },
				// {"BufferID", String.valueOf(obj.getInt("bufferId"))},
				{ "Cookie", flow.getCookie() },
				{ "Idle Timeout", flow.getIdleTimeOut() },
				{ "Hard Timeout", flow.getHardTimeOut() },
				// {"Flags", String.valueOf(obj.getInt("flags"))},
				// {"Command", String.valueOf(obj.getInt("command"))},
				{ "Out Port", flow.getOutPort() } };
				// {"Type", obj.getString("type")},
				// {"Version", String.valueOf(obj.getInt("version"))},
				// {"X ID", String.valueOf(obj.getInt("xid"))}};

		return f;
			}
	
	// Gets an action by it's index and formats it so that it can be displayed in a table
		public static String[][] getNewFlowTableFormat(){
				
			String[][] f = { { "Name",},
					{ "Actions", "..." },
					{ "Match", "..." },
					{ "Priority"},
					// {"BufferID", String.valueOf(obj.getInt("bufferId"))},
					{ "Cookie"},
					{ "Idle Timeout"},
					{ "Hard Timeout"},
					// {"Flags", String.valueOf(obj.getInt("flags"))},
					// {"Command", String.valueOf(obj.getInt("command"))},
					{ "Out Port"}};
					// {"Type", obj.getString("type")},
					// {"Version", String.valueOf(obj.getInt("version"))},
					// {"X ID", String.valueOf(obj.getInt("xid"))}};

			return f;
				}

}
