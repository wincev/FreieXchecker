package com.freiexchange.freiexchange;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainAdapter extends BaseAdapter {

    private Context context; //context
    private ArrayList<MainItem> items; //data source of the list adapter
    private static DecimalFormat df2 = new DecimalFormat("0.00000000");

    public MainAdapter(Context context, ArrayList<MainItem> itemsArrayList) {
        this.context = context;
        this.items = itemsArrayList;
    }

    @Override
    public int getCount() {
        return items.size(); //returns total of items in the list
    }

    @Override
    public Object getItem(int position) {
        return items.get(position); //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.main_layout_list_view_row_items, parent, false);
        }

        // get current item to be displayed
        MainItem currentItem = (MainItem) getItem(position);

        // get the TextView for item name and item description
        TextView textTicker = convertView.findViewById(R.id.ticker);
        TextView textPrice = convertView.findViewById(R.id.tickerPrice);

        //sets the text for item name and item description from the current item object
        textTicker.setText(currentItem.getTickerName());
        textPrice.setText(currentItem.getTickerPrice());

        // returns the view for the current row
        return convertView;
    }
}


