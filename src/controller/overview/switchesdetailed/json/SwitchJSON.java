package controller.overview.switchesdetailed.json;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import controller.floodlightprovider.FloodlightProvider;
import controller.util.Deserializer;

public class SwitchJSON {

	private final static int THREADS = Runtime.getRuntime().availableProcessors();
    public static ExecutorService executor = Executors.newFixedThreadPool(THREADS);
	public static String IP = FloodlightProvider.getIP();
    
	public static Future<Object> startSwitchRestCalls(final String dpid, final boolean update) {
        Future<Object> future = executor.submit(new Callable<Object>() {
            public Object call() {
            	
            	Future<Object> futureDescription = null, futureAggregate = null, futureFlow, futurePort, futureFeatures;
            	Map<String, Future<Object>> requests = new HashMap<String, Future<Object>>();
            	
            	if(!update){
            	futureDescription =  Deserializer.readJsonObjectFromURL("http://" + IP
    					+ ":8080/wm/core/switch/" + dpid
    					+ "/desc/json");
            	requests.put("description", futureDescription);
            	}
        	
            	futureAggregate =  Deserializer.readJsonObjectFromURL("http://" + IP
        					+ ":8080/wm/core/switch/" + dpid
        					+ "/aggregate/json");
            	requests.put("aggregate", futureAggregate);
            	
            	futureFlow = Deserializer.readJsonObjectFromURL("http://" + IP
        					+ ":8080/wm/core/switch/" + dpid + "/flow/json");
            	requests.put("flow", futureFlow);
        			
            	futurePort = Deserializer
        					.readJsonObjectFromURL("http://" + IP
        							+ ":8080/wm/core/switch/" + dpid
        							+ "/port/json");
            	requests.put("port", futurePort);
        			
            	futureFeatures = Deserializer
        					.readJsonObjectFromURL("http://" + IP
        							+ ":8080/wm/core/switch/" + dpid
        							+ "/features/json");	
            	requests.put("features", futureFeatures);
        			
    			
    			return requests;
            }
        });
        return future;
    }
}
