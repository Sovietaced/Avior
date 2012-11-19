package controller.tools.flowmanager.table;

import org.eclipse.swt.widgets.TableItem;

import view.tools.flowmanager.StaticFlowManager;
import controller.util.ErrorCheck;
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
	
		// Checks the entries for valid values
		public static boolean errorChecksPassed(TableItem[] items){
			
					if(!(items[3].getText(1).isEmpty())){
						if(!ErrorCheck.isNumeric(items[3].getText(1))){
							StaticFlowManager.displayError("Priority must be a valid number.");
							return false;
						}
					}			
				return true;
			}

}
