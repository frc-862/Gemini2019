package frc.lightning.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class CANConfig {
    public static Object getJSON(String urlToRead) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlToRead);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        var parser = new JSONParser();
        var json = parser.parse(rd);
        rd.close();

        return json;
    }

    public static Object getCANDevices() {
        try {
            return getJSON("http://127.0.0.1:1250/?action=getversion");
        } catch (Exception e) {
            System.err.println(e);
            return new JSONObject();
        }
    }
}
