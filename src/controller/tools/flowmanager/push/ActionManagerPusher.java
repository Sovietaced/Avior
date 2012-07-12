package controller.tools.flowmanager.push;

import java.util.List;

import model.tools.flowmanager.Action;

import org.eclipse.swt.widgets.TableItem;

import view.tools.flowmanager.FlowManager;

public class ActionManagerPusher {

	public static List<Action> addAction(TableItem[] items, String actionType,
			int currActionIndex) {

		List<Action> actions = removeAction(currActionIndex);

		if (actionType.equals("enqueue")) {
			actions.add(new Action(items[2].getText(1), items[0].getText(1)
					+ ":" + items[1].getText(1)));
		} else if (actionType.equals("strip-vlan")) {
			actions.add(new Action(items[0].getText(1)));
		} else {
			actions.add(new Action(items[1].getText(1), items[0].getText(1)));
		}

		return actions;
	}

	public static List<Action> addNewAction(TableItem[] items, String actionType) {

		List<Action> actions = FlowManager.getActions();

		if (actionType.equals("enqueue")) {
			actions.add(new Action(items[2].getText(1), (items[0].getText(1)
					+ ":" + items[1].getText(1))));
		} else if (actionType.equals("strip-vlan")) {
			actions.add(new Action(items[0].getText(1)));
		} else {
			actions.add(new Action(items[1].getText(1), items[0].getText(1)));
		}

		return actions;
	}

	public static List<Action> removeAction(int index) {
		List<Action> actions = FlowManager.getActions();
		actions.remove(index);
		return actions;
	}

	public static List<Action> removeAllActions() {
		List<Action> actions = FlowManager.getActions();
		actions.clear();
		return actions;
	}
}
