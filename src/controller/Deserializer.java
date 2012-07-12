package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import avior.json.JSONArray;
import avior.json.JSONException;
import avior.json.JSONObject;

public class Deserializer {

	public static InputStream is;

	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	public static JSONArray readJsonArrayFromURL(String url)
			throws IOException, JSONException {
		is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is,
					Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONArray jsonArr = new JSONArray(jsonText);
			return jsonArr;
		} finally {
			is.close();
		}
	}

	public static JSONObject readJsonObjectFromURL(String url)
			throws IOException, JSONException {
		is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is,
					Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONObject jsonObj = new JSONObject(jsonText);
			return jsonObj;
		} finally {
			is.close();
		}
	}
}
