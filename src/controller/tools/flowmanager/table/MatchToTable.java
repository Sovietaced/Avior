package controller.tools.flowmanager.table;

import java.io.IOException;
import java.util.List;

import org.eclipse.swt.widgets.TableItem;

import controller.util.ErrorCheck;
import controller.util.JSONException;

import model.overview.Port;
import model.overview.Switch;
import model.tools.flowmanager.Match;
import view.tools.flowmanager.MatchManager;
import view.tools.flowmanager.StaticFlowManager;

public class MatchToTable {

	// Gets a match and formats it appropriately for the table
	public static String[][] getMatchTableFormat() throws JSONException,
			IOException {

		Match m = StaticFlowManager.getMatch();
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

	// Returns empty values
	public static String[][] getNewMatchTableFormat() {
		String[][] match = { { "Data Layer Destination", "" },
				{ "Data Layer Source", "" }, { "Data Layer Type", "" },
				{ "Data Layer VLAN", "" }, { "Data Layer PCP", "" },
				{ "Input Port", "" }, { "Network Destination", "" },
				{ "Network Protocol", "" }, { "Network Source", "" },
				{ "Network Type Of Service", "" },
				{ "Transport Destination", "" }, { "Transport Source", "" },
				{ "Wildcards", "" } };
		return match;
	}

	// Checks the entries for valid values, also checks if port entries are
	// valid
	public static boolean errorChecksPassed(Switch sw, TableItem[] items) {

		List<Port> ports = sw.getPorts();
		boolean checkPorts = false;

		if (!items[0].getText(1).isEmpty()
				&& !ErrorCheck.isMac(items[0].getText(1))) {
			MatchManager
					.displayError("Data Layer Destination must be a valid MAC address.");
			return false;
		}
		if (!items[1].getText(1).isEmpty()
				&& !ErrorCheck.isMac(items[1].getText(1))) {
			MatchManager
					.displayError("Data Layer Source must be a valid MAC address.");
			return false;
		}
		if (!items[3].getText(1).isEmpty()
				&& !ErrorCheck.isNumeric(items[3].getText(1))) {
			MatchManager
					.displayError("Data Layer VLAN must be a valid number.");
			return false;
		}
		if (!items[4].getText(1).isEmpty()
				&& !ErrorCheck.isNumeric(items[4].getText(1))) {
			MatchManager.displayError("Data Layer PCP must be a valid number.");
			return false;
		}
		if (!items[5].getText(1).isEmpty()
				&& !ErrorCheck.isNumeric(items[5].getText(1))) {
			MatchManager.displayError("Input Port must be a valid number.");
			return false;
		} else if (!items[5].getText(1).isEmpty()
				&& ErrorCheck.isNumeric(items[5].getText(1))) {
			checkPorts = true;
		}

		if (!items[6].getText(1).isEmpty()
				&& !ErrorCheck.isIP(items[6].getText(1))) {
			MatchManager
					.displayError("Network Destination must be a valid IP address.");
			return false;
		}
		if (!items[7].getText(1).isEmpty()
				&& !ErrorCheck.isNumeric(items[7].getText(1))) {
			MatchManager
					.displayError("Network Protocol must be a valid number.");
			return false;
		}
		if (!items[8].getText(1).isEmpty()
				&& !ErrorCheck.isIP(items[8].getText(1))) {
			MatchManager
					.displayError("Network Source must be a valid IP address.");
			return false;
		}
		if (!items[9].getText(1).isEmpty()
				&& !ErrorCheck.isNumeric(items[9].getText(1))) {
			MatchManager
					.displayError("Network Type of Service must be a valid number.");
			return false;
		}
		if (!items[10].getText(1).isEmpty()
				&& !ErrorCheck.isNumeric(items[10].getText(1))) {
			MatchManager
					.displayError("Transport Destination must be a valid number.");
			return false;
		}
		if (!items[11].getText(1).isEmpty()
				&& !ErrorCheck.isNumeric(items[11].getText(1))) {
			MatchManager
					.displayError("Transport Source must be a valid number.");
			return false;
		}
		if (!items[12].getText(1).isEmpty()
				&& !ErrorCheck.isNumeric(items[12].getText(1))) {
			MatchManager.displayError("Wildcards must be a valid number.");
			return false;
		}

		if (checkPorts) {
			for (Port port : ports) {
				if (items[5].getText(1).equals(port.getPortNumber())) {
					return true;
				}
			}

			MatchManager
					.displayError("That port does not exist on the switch!");
			return false;
		}
		return true;
	}
}
