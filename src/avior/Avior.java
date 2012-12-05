package avior;

import view.Startup;

public class Avior {

	public static void main(String[] args) {
		try {
			new Startup();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}