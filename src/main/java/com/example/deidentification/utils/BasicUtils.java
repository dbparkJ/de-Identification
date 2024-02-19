package com.example.deidentification.utils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BasicUtils {
    // URL에서 JSON 데이터를 가져와 JSONObject로 반환하는 메서드
    public static JSONObject fetchJsonFromUrl(String urlString) {
        JSONObject jsonObject = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                System.err.println("HTTP GET Request Failed with Error code : " + responseCode);
                return null;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            jsonObject = new JSONObject(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

}
