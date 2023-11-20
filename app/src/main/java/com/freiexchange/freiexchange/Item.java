package com.freiexchange.freiexchange;

import android.text.Editable;
import org.json.JSONException;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Item {
    private String name;
    private String balance;
    private boolean state;

    public Item(String name, String balance, boolean state) {
        this.name = name;
        this.balance = balance;
        this.state = state;
    }

    public static ArrayList<Item> getEmptyItemList() throws Exception {
        ArrayList<Item> items = new ArrayList<>();
        ArrayList<MainItem> mainItems = Ticker.getArrayFromTicker();
        for (int i = 0; i < mainItems.size(); i++) {
            Item newItem = new Item(mainItems.get(i).getTickerName(), "0", false);
            items.add(newItem);
        }
        return items;
    }

    public String getItemName() {
        return this.name;
    }

    public String getItemBal() {
        return balance;
    }

    public boolean getItemState() {
        return state;
    }

    public void setItemName(String name) {
        this.name = name;
    }

    public void setItemBal(String balance) {
        this.balance = balance;
    }

    public void setItemState(boolean state) {
        this.state = state;
    }


}

