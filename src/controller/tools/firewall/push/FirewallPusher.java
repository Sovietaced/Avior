package controller.tools.firewall.push;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.eclipse.swt.widgets.TableItem;

import view.tools.firewall.Firewall;
import view.util.DisplayMessage;
import controller.floodlightprovider.FloodlightProvider;
import controller.util.ErrorCheck;
import controller.util.JSONException;
import controller.util.JSONObject;

import model.tools.firewall.Rule;

public class FirewallPusher {
    
    private static String IP = FloodlightProvider.getIP();
    private static String PORT = FloodlightProvider.getPort();
    private static String jsonResponse;
    
    public static String push(Rule rule) throws IOException, JSONException{
        
        jsonResponse = "";
        URL url = new URL("http://" + IP
                + ":" + PORT + "/wm/firewall/rules/json");
        
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(rule.serialize());
        wr.flush();

        // Get the response
        BufferedReader rd = new BufferedReader(new InputStreamReader(
                conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            jsonResponse = jsonResponse.concat(line);
        }
        wr.close();
        rd.close();

        // Wrap the response
        JSONObject json = new JSONObject(jsonResponse);
        
        // Make sure the firewall pusher throws no errors
        if (json.getString("status").equals("Rule added") ) {
            for (Rule r : FloodlightProvider.getRules()) {
                System.out.println("r:" + r.serialize());
                System.out.println("rule" + rule.serialize()); 
                if (rule.equals(r)) {
                    return "Rule successfully pushed!";
                }
            }
            // If not found give user guidance
            return "Rule was not pushed... Check your controller log for more details.";
        } else {
            return json.getString("status");
        }
    }
    
    public static String remove(Rule rule) throws IOException, JSONException{
        
        jsonResponse = "";

        URL url = new URL("http://" + IP
                + ":" + PORT + "/wm/firewall/rules/json");
        HttpURLConnection connection = null;
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        // We have to override the post method so we can send data
        connection.setRequestProperty("X-HTTP-Method-Override", "DELETE");
        connection.setDoOutput(true);

        // Send request
        OutputStreamWriter wr = new OutputStreamWriter(
                connection.getOutputStream());
        wr.write(rule.deleteString());
        wr.flush();

        // Get Response
        BufferedReader rd = new BufferedReader(new InputStreamReader(
                connection.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            jsonResponse = jsonResponse.concat(line);
        }
        wr.close();
        rd.close();

        JSONObject json = new JSONObject(jsonResponse);
        // Return result string from key "status"
        return json.getString("status");
    }
    
    public static void removeAll(){
        List<Rule> rules = FloodlightProvider.getRules();
        
        for(Rule rule : rules){
            try {
                remove(rule);
            } catch (IOException | JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
     
    public static Rule parseTableChanges(Rule rule, TableItem[] items) {
        
        if (!items[1].getText(1).isEmpty())
            rule.setDl_src(items[1].getText(1));
        if (!items[2].getText(1).isEmpty())
            rule.setDl_dst(items[2].getText(1));
        if (!items[3].getText(1).isEmpty())
            rule.setDl_type(items[3].getText(1));
        if (!items[4].getText(1).isEmpty())
            rule.setSrcIP(items[4].getText(1));
        if (!items[5].getText(1).isEmpty())
            rule.setDstIP(items[5].getText(1));
        if (!items[6].getText(1).isEmpty())
            rule.setNw_proto(items[6].getText(1));
        if (!items[7].getText(1).isEmpty())
            rule.setTp_src(items[7].getText(1));
        if (!items[8].getText(1).isEmpty())
            rule.setTp_dst(items[8].getText(1));

        return rule;
    }

    public static boolean errorChecksPassed(TableItem[] items) {
        if(!items[1].getText(1).isEmpty()){
            if(!ErrorCheck.isMac(items[1].getText(1))){
                DisplayMessage.displayError(Firewall.getShell(),"Src MAC must be a valid MAC address!");
                return false;
            }
        }
        if(!items[2].getText(1).isEmpty()){
            if(!ErrorCheck.isMac(items[2].getText(1))){
                DisplayMessage.displayError(Firewall.getShell(),"Dst MAC must be a valid MAC address!");
                return false;
            }
        }
        if(!items[4].getText(1).isEmpty()){
            if(!ErrorCheck.isIPandSubnet(items[4].getText(1))){
                DisplayMessage.displayError(Firewall.getShell(),"Src IP must be a valid IP address with sunet mask ie. 10.0.0.1/32!");
                return false;
            }
        }
        if(!items[5].getText(1).isEmpty()){
            if(!ErrorCheck.isIPandSubnet(items[5].getText(1))){
                DisplayMessage.displayError(Firewall.getShell(),"Dst IP must be a valid IP address with sunet mask ie. 10.0.0.1/32!");
                return false;
            }
        }
        if(!items[6].getText(1).isEmpty()){
            if(!items[6].getText(1).equals("TCP")
                    && !items[6].getText(1).equals("UDP")
                    && !items[6].getText(1).equals("ICMP")){
                DisplayMessage.displayError(Firewall.getShell(),"Network Protocol must be TCP, UDP, or ICMP");
                return false;
            }
        }
        if(!items[7].getText(1).isEmpty()){
            if(!ErrorCheck.isNumeric(items[7].getText(1))){
                DisplayMessage.displayError(Firewall.getShell(),"Transport source must be a valid number!");
                return false;
            }
        }
        if(!items[8].getText(1).isEmpty()){
            if(!ErrorCheck.isNumeric(items[8].getText(1))){
                DisplayMessage.displayError(Firewall.getShell(),"Transport destination must be a valid number!");
                return false;
            }
        }
        return true;
    }
}
