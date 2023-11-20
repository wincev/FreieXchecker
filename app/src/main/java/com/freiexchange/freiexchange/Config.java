package com.freiexchange.freiexchange;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Config extends AppCompatActivity{

    ListView lvPairs;
    PairsAdapter adapter;
    SharedPreferences pairs;
    EditText btcBalance;
    String FREIPREFS = "freiex";
    String BTCBAL = "btcBal";
    String TICKER = "ticker";
    Button deleteButton;
    AlertDialog.Builder dialog;

    public static Intent makeIntent(Context contClass) {
        return new Intent(contClass, Config.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config);

        deleteButton = findViewById(R.id.btnReset);
        dialog = new AlertDialog.Builder(this);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setTitle("ATTENTION")
                        .setMessage("Delete all Settings?")
                        .setCancelable(true)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(Config.this, "Reset Favorites.", Toast.LENGTH_SHORT)
                                        .show();
                                try {
                                    String emptyString = "";
                                    btcBalance.setText(emptyString);
                                    fillListView(true);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i){
                                dialogInterface.cancel();
                            }
                        })
                        .show();
            }
        });

        btcBalance = findViewById(R.id.TbtcBal);
        btcBalance.setText(getSharedPrefs(BTCBAL));
        lvPairs  = findViewById(R.id.Lpairs);
        backButtonOnClick();
        //resetButtonOnClick();
        try {
            fillListView(false);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    protected void onPause() {
        Gson gson = new Gson();
        String gsonStringArray = gson.toJson(getListFavorites());
        setSharedPrefs(TICKER, gsonStringArray);
        setSharedPrefs(BTCBAL, btcBalance.getText().toString());
        super.onPause();
    }

    private void resetButtonOnClick() {
        Button addButton = findViewById(R.id.btnReset);
        addButton.setOnClickListener(v -> {
            Toast.makeText(Config.this, "Reset Favorites.", Toast.LENGTH_SHORT)
                    .show();
            try {
                String emptyString = "";
                btcBalance.setText(emptyString);
                fillListView(true);
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }

    public void backButtonOnClick() {
        Button btn = findViewById(R.id.btnChart);
        btn.setOnClickListener(v -> {
            Toast.makeText(Config.this, "Opening chart view.", Toast.LENGTH_SHORT)
                    .show();
            Intent intent = Portfolio.makeIntent(Config.this);
            startActivity(intent);
        });

        // btn.setOnClickListener(v -> finish());
    }

    private ArrayList getListFavorites() {
        ArrayList<Item> newList = new ArrayList<>();
        for (int i = 0; i < adapter.getCount(); i++) {
            Item item = adapter.getItem(i);

            //if (item.getItemState() == true || Double.parseDouble(item.getItemBal()) != 0) {
            newList.add(item);
            //}
        }
        return newList;
    }

    private void fillListView(boolean reset) throws Exception {
        // Setup the data source

        ArrayList<Item> itemsArrayList = getArrayFromMemory(reset); // calls function to get items list

        adapter = new PairsAdapter(this, itemsArrayList);
        lvPairs.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lvPairs.setAdapter(adapter);
    }

    public String getSharedPrefs(String pair) {
        pairs = getSharedPreferences(FREIPREFS, Context.MODE_PRIVATE);
        String pairsString = pairs.getString(pair, "");
        return pairsString;
    }

    public void setSharedPrefs(String key, String value) {
        pairs = getSharedPreferences(FREIPREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = pairs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public ArrayList<Item> getArrayFromMemory(boolean reset) throws Exception {
        String sharedPrefs = getSharedPrefs(TICKER);
        ArrayList<Item> items = new ArrayList<>();

        //return an empty array if reset is true or no array is stored yet
        if (!sharedPrefs.isEmpty() && reset == false) {
            ArrayList itemsArray = new Gson().fromJson(sharedPrefs, ArrayList.class);
            for (int i = 0; i < itemsArray.size(); i++) {
                JSONObject jsonObject = new JSONObject(itemsArray.get(i).toString());
                Item newItem = new Item(jsonObject.getString("name"), jsonObject.getString("balance"), jsonObject.getBoolean("state"));
                items.add(newItem);
            }
        } else items = Item.getEmptyItemList();

        Gson gson = new Gson();
        String listTickerString = gson.toJson(items);
        setSharedPrefs(listTickerString, TICKER);

        return items;
    }

}
