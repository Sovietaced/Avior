package avior;

import java.io.IOException;
import java.net.InetAddress;

import view.Gui;
import view.Startup;

public class Avior {

	public static void main(String[] args) {
	    
	    if(args.length == 1){
	        String arg = args[0];
    	        try {
                    if (InetAddress.getByName(arg).isReachable(5000)) {
                        // Here we dispose this screen and launch the GUI
                        new Gui(arg);
                    } else {
                        System.out.println("Invalid launchparameter, going to main screen.");
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