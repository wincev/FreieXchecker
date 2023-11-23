package com.freiexchange.freiexchange;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.*;

public class MainItem {
    private String tickerName;
    private String tickerPrice;

    public MainItem(String name, String price) {
        //Main Items can be either a Ticker/price OR a Date/Portfoliobalance
        this.tickerName = name;
        this.tickerPrice = price;
    }

    public static ArrayList<MainItem> mainItemsFromJson(String itemsString) throws JSONException {
        // Uses the Freiexchange api Ticker String to create a ArrayList of MainItems
        ArrayList<MainItem> mainItemsArray = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(itemsString);
        Iterator<String> arrayKeys = jsonObject.keys();
        List<String> keyStringList = new ArrayList<>();
        while (arrayKeys.hasNext()){
          keyStringList.add(arrayKeys.next());
        }
        Collections.sort(keyStringList);

        for (int i = 0; i < keyStringList.size(); i++) {
            String stringListItem = keyStringList.get(i);
            if (jsonObject.getJSONArray(stringListItem).length() > 0 && jsonObject.getJSONArray(stringListItem).getJSONObject(0).has("last")) {
                MainItem newMain = new MainItem(stringListItem, jsonObject.getJSONArray(stringListItem).getJSONObject(0).getString("last"));
                mainItemsArray.add(newMain);
            }
        }

        return mainItemsArray;
    }

    public static ArrayList<MainItem> mainItemsFromJsonString(String itemsString) throws JSONException {
        // Uses the oldPrefs String
        ArrayList<MainItem> mainItemsArray;
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<MainItem>>() {}.getType();
        mainItemsArray = gson.fromJson(itemsString, type);

        return mainItemsArray;
    }

    public static ArrayList<MainItem> getMainItemArrayFromItems(ArrayList<Item> itemsArray){
        ArrayList<MainItem> mainItemsArray = new ArrayList<>();
        for (int i = 0; i < itemsArray.size(); i++) {
            MainItem newItem = new MainItem(itemsArray.get(i).getItemName(), itemsArray.get(i).getItemBal());
            mainItemsArray.add(newItem);
        }
        return mainItemsArray;
    }

    public String getTickerName() {
        return this.tickerName;
    }

    public String getTickerPrice() {
        return tickerPrice;
    }

    public void setTickerName(String name) {
        this.tickerName = name;
    }

    public void setTickerPrice(String price) {
        this.tickerPrice = price;
    }
}

