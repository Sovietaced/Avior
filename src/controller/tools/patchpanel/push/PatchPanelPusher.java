package controller.tools.patchpanel.push;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.tools.flowmanager.Action;
import model.tools.flowmanager.Flow;
import model.tools.flowmanager.Match;

import org.eclipse.swt.widgets.TableItem;

import controller.tools.flowmanager.push.FlowManagerPusher;
import controller.util.JSONException;

public class PatchPanelPusher {

	// Here we create two flows for the patch panel, incoming and outgoing
	public static String push(TableItem[] items, String sw){
		Match match = new Match();
		List<Action> actions = new ArrayList<Action>();
		
		// First Flow
		Flow firstFlow = new Flow(sw);
		firstFlow.setName("pp-" + items[0].getText(1) + "to" + items[0].getText(2));
		match.setInputPort(items[0].getText(1));
		firstFlow.setMatch(match);
		actions.add(new Action("output", items[0].getText(2)));
		firstFlow.setActions(actions);
		
		// Second Flow
		Flow secondFlow = new Flow(sw);
		actions = new ArrayList<Action>();
		match = new Match();
		secondFlow.setName("pp-" + items[0].getText(2) + "to" + items[0].getText(1));
		match.setInputPort(items[0].getText(2));
		secondFlow.setMatch(match);
		actions.add(new Action("output", items[0].getText(1)));
		secondFlow.setActions(actions);
		
		String response = "Failed to push patch panel flows";
		try {
			response = FlowManagerPusher.push(firstFlow);
			response = response.concat(FlowManagerPusher.push(secondFlow));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return response;
	}
}
