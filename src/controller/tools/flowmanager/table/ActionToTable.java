package controller.tools.flowmanager.table;

import java.util.List;

import model.tools.flowmanager.Action;
import view.tools.flowmanager.StaticFlowManager;

public class ActionToTable {

	// Gets an action by it's index and formats it so that it can be displayed
	// in a table
	public static String[][] getActionTableFormat(int index) {
		List<Action> actions = StaticFlowManager.getActions();
		Action action = actions.get(index);

		if (action.getType().equals("strip-vlan")) {
			String[][] act = { { "Type", action.getType() } };
			return act;
		} else if (action.getType().equals("enqueue")) {
			StringBuilder param = new StringBuilder(action.getParam());
			StringBuilder value = new StringBuilder(action.getValue());
			String[][] act = {
					{ param.substring(0, param.indexOf(":")),
							value.substring(0, value.indexOf(":")) },
					{
							param.substring(param.indexOf(":") + 1,
									param.length()),
							value.substring(value.indexOf(":") + 1,
									value.length()) },
					{ "Type", action.getType() } };
			return act;
		} else {
			String[][] act = { { action.getParam(), action.getValue() },
					{ "Type", action.getType() } };
			return act;
		}
	}

	public static String[][] getNewActionTableFormat(String actionType) {

		Action action = new Action(actionType);

		if (action.getType().equals("strip-vlan")) {
			String[][] act = { { "Type", action.getType() } };
			return act;
		} else if (action.getType().equals("enqueue")) {
			StringBuilder param = new StringBuilder(action.getParam());
			String[][] act = {
					{ param.substring(0, param.indexOf(":")), "" },
					{ param.substring(param.indexOf(":") + 1, param.length()),
							"" }, { "Type", action.getType() } };
			return act;
		} else {
			String[][] act = { { action.getParam(), action.getValue() },
					{ "Type", action.getType() } };
			return act;
		}
	}
}