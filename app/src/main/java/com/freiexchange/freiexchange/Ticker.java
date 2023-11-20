package com.freiexchange.freiexchange;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicReference;

public class Ticker {

    public static String getTickerString() throws InterruptedException {

        final String[] stringIn = new String[1];
        Thread thread = new Thread(() -> {

            try {
                InputStream isFx = new URL("https://api.freiexchange.com/public/ticker").openStream();
                BufferedReader rdFx = new BufferedReader(new InputStreamReader(isFx, Charset.forName("UTF-8")));
                StringBuilder sb = new StringBuilder();
                int cp;
                while ((cp = rdFx.read()) != -1) {
                    sb.append((char) cp);
                }
                stringIn[0] = sb.toString();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        thread.start();
        thread.join();
        String outString = stringIn[0];

        return outString;
    }

    public static ArrayList<MainItem> getArrayFromTicker() throws Exception {
        String tickerString = getTickerString();
        ArrayList<MainItem> tickerArray = MainItem.mainItemsFromJson(tickerString);

        return tickerArray;
    }

}
