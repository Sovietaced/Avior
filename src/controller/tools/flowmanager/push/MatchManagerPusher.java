package controller.tools.flowmanager.push;

import model.tools.flowmanager.Match;

import org.eclipse.swt.widgets.TableItem;

public class MatchManagerPusher {

	public static Match addMatch(TableItem[] items) {

		Match match = new Match();
		if (!items[0].getText(1).isEmpty())
			match.setDataLayerDestination(items[0].getText(1));
		if (!items[1].getText(1).isEmpty())
			match.setDataLayerSource(items[1].getText(1));
		if (!items[2].getText(1).isEmpty())
			match.setDataLayerType(items[2].getText(1));
		if (!items[3].getText(1).isEmpty())
			match.setDataLayerVLAN(items[3].getText(1));
		if (!items[4].getText(1).isEmpty())
			match.setDataLayerPCP(items[4].getText(1));
		if (!items[5].getText(1).isEmpty())
			match.setInputPort(items[5].getText(1));
		if (!items[6].getText(1).isEmpty())
			match.setNetworkDestination(items[6].getText(1));
		if (!items[7].getText(1).isEmpty())
			match.setNetworkProtocol(items[7].getText(1));
		if (!items[8].getText(1).isEmpty())
			match.setNetworkSource(items[8].getText(1));
		if (!items[9].getText(1).isEmpty())
			match.setNetworkTypeOfService(items[9].getText(1));
		if (!items[10].getText(1).isEmpty())
			match.setTransportDestination(items[10].getText(1));
		if (!items[11].getText(1).isEmpty())
			match.setTransportSource(items[11].getText(1));
		if (!items[12].getText(1).isEmpty())
			match.setWildcards(items[12].getText(1));

		return match;
	}

	// TODO REMOVE MATCH
}
