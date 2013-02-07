package controller.floodlightprovider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import view.Gui;

import controller.overview.switchesdetailed.json.FlowJSON;
import controller.overview.switchesdetailed.json.SwitchesJSON;
import controller.tools.firewall.json.RuleJSON;
import controller.tools.flowmanager.json.StaticFlowManagerJSON;
import controller.util.JSONException;

import model.overview.Switch;
import model.tools.firewall.Rule;
import model.tools.flowmanager.Flow;

public class FloodlightProvider {
    
    private static List<Switch> switches = new ArrayList<Switch>();
    private static List<Flow> staticFlows = new ArrayList<Flow>();
    private static List<Flow> realFlows = new ArrayList<Flow>();
    
    /**
     * 
     * @return The list of switches
     */
    public static List<Switch> getSwitches(boolean update) {
        if(update){
            try {
                switches = SwitchesJSON.getSwitches();
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
            return switches;
        }
        else{
            return switches;
        }
    }
    
    public static List<String> getSwitchDpids(){

       List<String> dpids = new ArrayList<String>();
       for(Switch sw : switches){
           dpids.add(sw.getDpid());
       }
        
        return dpids;
    }
    
    /**
     * 
     * @return The list of switches
     */
    public static Switch getSwitch(String dpid, boolean update) {
        for (Switch sw : switches){
            if(sw.getDpid().equals(dpid)){
                if(update){
                    try {
                        SwitchesJSON.updateSwitch(sw);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                return sw;
            }
        }
        return null;
    }

    public static List<Flow> getStaticFlows(String dpid, boolean update) {
        if(update){
            try {
                staticFlows = StaticFlowManagerJSON.getFlows(dpid);
            } catch (IOException | JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return staticFlows;
        }
        else{
            return staticFlows;
        }
    }   
    
    public static List<Flow> getRealFlows(String dpid, boolean update) {
        if(update){
            try {
                realFlows = FlowJSON.getFlows(dpid);
            } catch (IOException | JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return realFlows;
        }
        else{
            return realFlows;
        }
    }   
    
    public static List<Rule> getRules(){
        try {
            return RuleJSON.getRules();
        } catch (JSONException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    
    public static String getIP(){
        return Gui.IP;
    }
}