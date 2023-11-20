package com.freiexchange.freiexchange;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView lvPrice;
    MainAdapter mainAdapter;
    SharedPreferences pairs;
    TextView textBal;
    TextView estBalance;
    String FREIPREFS = "freiex";
    String BTCBAL = "btcBal";
    String ESTBALLIST = "estBalList";
    String TICKER = "ticker";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textBal = findViewById(R.id.TtextBal);
        textBal.setText("Estimated value in BTC:");
        estBalance = findViewById(R.id.TestBal);
        lvPrice = findViewById(R.id.Lprice);
        confButtonOnClick();
        checkButtonOnClick();
    }

    private void checkButtonOnClick() {
        Button addButton = findViewById(R.id.Bcheck);
        addButton.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Checking price.", Toast.LENGTH_SHORT)
                    .show();
            try {
                fillPriceListView();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void fillPriceListView() throws Exception {
        ArrayList<Item> memoryArray = getArrayFromMemory();
        ArrayList<MainItem> tickerArray = Ticker.getArrayFromTicker();
        ArrayList<MainItem> showedItemsArray = new ArrayList<>();
        String btcBalFromMemory = getSharedPrefs(BTCBAL);
        //fill list to display and calculate estimated balance
        if (btcBalFromMemory.isEmpty()){
            btcBalFromMemory = "0";
        }
        double btcBal = Double.parseDouble(btcBalFromMemory);
        if (memoryArray.isEmpty()) {
            showedItemsArray = tickerArray;
        } else {
            double ltcBal = 0;
            JSONObject jsonTicker = new JSONObject();
            for (int i = 0; i < tickerArray.size(); i++) {
                jsonTicker.put(tickerArray.get(i).getTickerName(), tickerArray.get(i).getTickerPrice());
            }
            for (int i = 0; i < memoryArray.size(); i++) {
                if (memoryArray.get(i).getItemState() == true && jsonTicker.has(memoryArray.get(i).getItemName())) {
                    MainItem mainItemFromJson = new MainItem(memoryArray.get(i).getItemName(), jsonTicker.getString(memoryArray.get(i).getItemName()));
                    showedItemsArray.add(mainItemFromJson);
                }
                if (Double.parseDouble(memoryArray.get(i).getItemBal()) != 0) {
                    //first sum LTC balances and BTC balances separately before the total sum
                    if (memoryArray.get(i).getItemName().substring(memoryArray.get(i).getItemName().length() -3) == "LTC")
                        ltcBal = ltcBal + (Double.parseDouble(memoryArray.get(i).getItemBal()) * Double.parseDouble(jsonTicker.getString(memoryArray.get(i).getItemName())));
                    else
                        btcBal = btcBal + (Double.parseDouble(memoryArray.get(i).getItemBal()) * Double.parseDouble(jsonTicker.getString(memoryArray.get(i).getItemName())));
                }
            }
            if (ltcBal != 0)
                btcBal = btcBal + ltcBal * Double.parseDouble(jsonTicker.getString("LTC_BTC"));
        }

        if (btcBal != 0) { //if no new value is found do nothing
            String oldPrefs = getSharedPrefs(ESTBALLIST);
            MainItem newEstBalItem = new MainItem(String.valueOf(System.currentTimeMillis() / 1000), String.valueOf(btcBal));
            ArrayList<MainItem> estBalItems = new ArrayList<>();
            if (!oldPrefs.isEmpty()) {
                estBalItems = MainItem.mainItemsFromJsonString(oldPrefs);
            }
            estBalItems.add(newEstBalItem);
            Gson gson = new Gson();
            String listTickerString = gson.toJson(estBalItems);
            setSharedPrefs(ESTBALLIST, listTickerString);
        }
        estBalance.setText(String.valueOf(btcBal));
        // instantiate the custom list adapter
        mainAdapter = new MainAdapter(this, showedItemsArray);
        lvPrice.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lvPrice.setAdapter(mainAdapter);
    }

    public void confButtonOnClick() {
        Button confButton = findViewById(R.id.Bconf);
        confButton.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Select pairs for portfolio view.", Toast.LENGTH_SHORT)
                    .show();
            Intent intent = Config.makeIntent(MainActivity.this);
            startActivity(intent);
        });

    }

    public String getSharedPrefs(String key) {
        pairs = getSharedPreferences(FREIPREFS, Context.MODE_PRIVATE);
        String pairString = pairs.getString(key, "");
        return pairString;
    }

    public void setSharedPrefs(String key, String value) {
        pairs = getSharedPreferences(FREIPREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = pairs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public ArrayList<Item> getArrayFromMemory() throws Exception {
        String sharedPrefs = getSharedPrefs(TICKER);
        ArrayList<Item> items = new ArrayList<>();

        if (!sharedPrefs.isEmpty()) {
            ArrayList itemsArray = new Gson().fromJson(sharedPrefs, ArrayList.class);
            for (int i = 0; i < itemsArray.size(); i++) {
                JSONObject jsonObject = new JSONObject(itemsArray.get(i).toString());
                Item newItem = new Item(jsonObject.getString("name"), jsonObject.getString("balance"), jsonObject.getBoolean("state"));
                items.add(newItem);
            }
        }

        return items;
    }




}