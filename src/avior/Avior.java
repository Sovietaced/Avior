package avior;

import java.io.IOException;
import java.net.InetAddress;

import controller.floodlightprovider.FloodlightProvider;

import view.Gui;
import view.Startup;

public class Avior {

	public static void main(String[] args) {
	    
	    if(args.length > 0){
	    	String IP;
	    	
	    	if(args.length == 2){
	    		FloodlightProvider.setPort(args[1]);
	    	}
	    	
	        IP = args[0];
    	        try {
                    if (InetAddress.getByName(IP).isReachable(5000)) {
                        // Here we dispose this screen and launch the GUI
                        new Gui(IP);
                    } else {
                        System.out.println("Could not reach controller from parameter specified, going to main screen.");
                    	try {
                    		new Startup();
                    	} catch (Exception e) {
                    		e.printStackTrace();
                    	}
                    }
                } catch (IOException e) {
                    //Fail silently
                }
		}
	    else{
	        try {
                new Startup();
            } catch (Exception e) {
                e.printStackTrace();
            }
	    }
	}
}