package controller.tools.firewall.table;

import java.util.ArrayList;
import java.util.List;

import model.overview.Port;
import model.tools.firewall.Rule;

public class RuleToTable {

	// This returns a table representation of the specified flow
	public static String[][] getRuleTableFormat(Rule rule) {

		String[][] r = {{ "Rule ID", String.valueOf(rule.getRuleid())},
				{ "Src MAC", rule.getDl_src(), String.valueOf(rule.isWildcard_dl_src())},
				{ "Dst MAC", rule.getDl_dst(), String.valueOf(rule.isWildcard_dl_dst())},
				{ "Datalayer Type", rule.getDl_type(), String.valueOf(rule.isWildcard_dl_type())},
				{ "Src IP", rule.getSrcIP(), String.valueOf(rule.isWildcard_nw_src())},
				{ "Dst IP", rule.getDstIP(), String.valueOf(rule.isWildcard_nw_dst())},
				{ "Network Procotol", rule.getNw_proto(), String.valueOf(rule.isWildcard_nw_proto())},
				{ "Transport Source", rule.getTp_src(), String.valueOf(rule.isWildcard_tp_src())},
				{ "Transport Destination", rule.getTp_dst(), String.valueOf(rule.isWildcard_tp_dst())}};
		return r;
	}

	// This returns a table representation of a new flow
	public static String[][] getNewRuleTableFormat() {

	    Rule rule = new Rule();
	    
	    String[][] r = {{ "Rule ID", "Cannot be set, generated after pushing!"},
                { "Src MAC", rule.getDl_src()},
                { "Dst MAC", rule.getDl_dst()},
                { "Datalayer Type", rule.getDl_type()},
                { "Src IP", rule.getSrcIP()},
                { "Dst IP", rule.getDstIP()},
                { "Network Procotol", rule.getNw_proto()},
                { "Transport Source", rule.getTp_src()},
                { "Transport Destination", rule.getTp_dst()}};
        return r;
	}

    public static String[] getPortComboFormat(List<Port> ports) {
        List<String> portList = new ArrayList<String>();
        
        for(Port p : ports){
            portList.add(p.getPortNumber());
        }
        
        return portList.toArray(new String[ports.size()]);
    }
}
