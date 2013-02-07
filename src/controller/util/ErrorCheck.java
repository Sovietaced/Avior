package controller.util;

import java.text.NumberFormat;
import java.text.ParsePosition;

public class ErrorCheck {

	public static boolean isNumeric(String str) {
		NumberFormat formatter = NumberFormat.getInstance();
		ParsePosition pos = new ParsePosition(0);
		formatter.parse(str, pos);
		return str.length() == pos.getIndex();
	}

	public static boolean isMac(String str) {
		return str
				.matches("([a-fA-F0-9]{2}[:\\-\\.]){5}[a-fA-F0-9]{2}");
	}

	public static boolean isIP(String str) {
		return str.matches("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
				+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
				+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
				+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
	}
	
	public static boolean isIPandSubnet(String ipSub){
	    String[] split = ipSub.split("/");
        if(!isIP(split[0])){
            return false;
        }
        if(split.length > 1){
            if(Integer.valueOf(split[1]) < 0 || Integer.valueOf(split[1]) > 32){
                return false;
            }
        }
        else{
            return false;
        }
        return true;
	}
}
