package controller.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.concurrent.Callable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class Deserializer {

	private final static int THREADS = Runtime.getRuntime().availableProcessors();
    public static ExecutorService executor = Executors.newFixedThreadPool(THREADS);


	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}
	
	public static Future<Object> readJsonArrayFromURL(final String surl){
		 final Future<Object> future = executor.submit(new Callable<Object>(){
           public Object call() {
       		try {
       			URL url = new URL(surl);
    			HttpURLConnection connection = null;
    			connection = (HttpURLConnection) url.openConnection();
    			connection.setRequestMethod("GET");
    			// We have to override the post method so we can send data
    			connection.setRequestProperty("Accept", "application/json");
    			connection.setDoOutput(true);

    			// Send request
    			InputStream is = connection.getInputStream();
       			BufferedReader rd = new BufferedReader(new InputStreamReader(is,
       					Charset.forName("UTF-8")));
       			String jsonText = readAll(rd);
       			is.close();
       			return new JSONArray(jsonText);
       		} catch (JSONException e) {
					System.out.println("Failed to deserialize data" +
							" : readJsonObjectFromURL");
					e.printStackTrace();
				} catch (IOException e) {
					System.out.println("Failed to deserialize data" +
							" : readJsonObjectFromURL");
					e.printStackTrace();
				}
       		return null;
           }
       });
		return future;
	}
	
	public static Future<Object> readJsonObjectFromURL(final String surl){
		 final Future<Object> future = executor.submit(new Callable<Object>(){
            public Object call() {
        		try {
        			URL url = new URL(surl);
        			HttpURLConnection connection = null;
        			connection = (HttpURLConnection) url.openConnection();
        			connection.setRequestMethod("GET");
        			// We have to override the post method so we can send data
        			connection.setRequestProperty("Accept", "application/json");
        			connection.setDoOutput(true);

        			// Send request
        			InputStream is = connection.getInputStream();
        			BufferedReader rd = new BufferedReader(new InputStreamReader(is,
        					Charset.forName("UTF-8")));
        			String jsonText = readAll(rd);
        			is.close();
        			return new JSONObject(jsonText);
        		} catch (JSONException e) {
					System.out.println("Failed to deserialize data" +
							" : readJsonObjectFromURL");
					e.printStackTrace();
				} catch (IOException e) {
					System.out.println("Failed to deserialize data" +
							" : readJsonObjectFromURL");
					e.printStackTrace();
				}
        		return null;
            }
        });
		return future;
	}
}
