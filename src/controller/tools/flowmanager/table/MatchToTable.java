package controller.tools.flowmanager.table;

import java.io.IOException;

import controller.util.JSONException;

import model.tools.flowmanager.Match;
import view.tools.flowmanager.FlowManager;

public class MatchToTable {

	// Gets a match and formats it appropriately
	public static String[][] getMatchTableFormat() throws JSONException,
			IOException {

		Match m = FlowManager.getMatch();
		String[][] match = {
				{ "Data Layer Destination", m.getDataLayerDestination() },
				{ "Data Layer Source", m.getDataLayerSource() },
				{ "Data Layer Type", m.getDataLayerType() },
				{ "Data Layer VLAN", m.getDataLayerVLAN() },
				{ "Data Layer PCP", m.getDataLayerPCP() },
				{ "Input Port", m.getInputPort() },
				{ "Network Destination", m.getNetworkDestination() },
				{ "Network Protocol", m.getNetworkProtocol() },
				{ "Network Source", m.getNetworkSource() },
				{ "Network Type Of Service", m.getNetworkTypeOfService() },
				{ "Transport Destination", m.getTransportDestination() },
				{ "Transport Source", m.getTransportSource() },
				{ "Wildcards", m.getWildcards() } };
		return match;
	}
}
