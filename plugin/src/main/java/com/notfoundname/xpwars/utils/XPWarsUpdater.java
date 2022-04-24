package com.notfoundname.xpwars.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class XPWarsUpdater {

    public static String getLatestVersionFromSpigot() throws IOException {
        URL url = new URL("https://api.spiget.org/v2/resources/76895/versions/latest");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.addRequestProperty("User-Agent", "Mozilla/5.0");
        InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());

        JsonElement jsonElement = new JsonParser().parse(inputStreamReader);

        if (jsonElement.isJsonObject()) {
            return jsonElement.getAsJsonObject().get("name").getAsString();
        }

        return "null";
    }
}
