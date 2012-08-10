package controller.tools.flowmanager.push;

import java.util.List;

import model.tools.flowmanager.Action;

import org.eclipse.swt.widgets.TableItem;

import view.tools.flowmanager.StaticFlowManager;

public class ActionManagerPusher {

	public static void addAction(TableItem[] items, String actionType) {

		List<Action> actions = StaticFlowManager.getActions();
		Action action;
		
		// Construct the action
		if(actionType.equals("enqueue")) {
			action = new Action(items[2].getText(1), items[0].getText(1)
					+ ":" + items[1].getText(1));
		} else if (actionType.equals("strip-vlan")) {
			action = new Action(items[0].getText(1));
		} else {
			action = new Action(items[1].getText(1), items[0].getText(1));
		}
		
		if(!actionExists(actions,action)){
			StaticFlowManager.setActions(actions);
			actions.add(action);
		}
	}
	
	// Checks to see if the action we want to add already exists. If so, it replaces the action
	public static boolean actionExists(List<Action> actions, Action action){
		for(Action act : actions){
			if(act.getType().equals(action.getType())){
				actions.set(actions.indexOf(act), action);
				StaticFlowManager.setActions(actions);
				return true;
			}
		}
		return false;
	}

	public static void removeAction(String actionType) {
		List<Action> actions = StaticFlowManager.getActions();
		int actionIndex = -1;
		for(Action act : actions){
			if(act.getType().equals(actionType)){
				actionIndex = actions.indexOf(act);
			}
		}
		if(actionIndex != -1)
		actions.remove(actionIndex);
		StaticFlowManager.setActions(actions);
	}

	public static void removeAllActions() {
		List<Action> actions = StaticFlowManager.getActions();
		actions.clear();
		StaticFlowManager.setActions(actions);
	}
}
