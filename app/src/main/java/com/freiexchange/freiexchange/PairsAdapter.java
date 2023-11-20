package com.freiexchange.freiexchange;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;

public class PairsAdapter extends ArrayAdapter<Item> {

    private Context context; //context
    private ArrayList<Item> items; //data source of the list adapter

    public PairsAdapter(Context context, ArrayList<Item> itemsArrayList) {
        super(context, R.layout.layout_list_view_row_items, itemsArrayList);
        this.context = context;
        this.items = itemsArrayList;
    }

    static class ViewHolder {
        protected TextView textViewHolder;
        protected EditText editTextHolder;
        protected CheckBox checkboxHolder;
    }

    @Override
    public int getCount() {
        return items.size(); //returns total of items in the list
    }

    @Override
    public Item getItem(int position) {
        return items.get(position); //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.layout_list_view_row_items, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textViewHolder = convertView.findViewById(R.id.text_view_item_name);
            viewHolder.editTextHolder = convertView.findViewById(R.id.text_view_item_description);
            viewHolder.checkboxHolder = convertView.findViewById(R.id.checkBox);
            viewHolder.checkboxHolder.setOnCheckedChangeListener((buttonView, isChecked) -> {
                int getPosition = (Integer) buttonView.getTag();
                items.get(getPosition).setItemState(buttonView.isChecked());
            });

            ViewHolder finalViewHolder = viewHolder;
            viewHolder.editTextHolder.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() != 0) {
                        int getPosition = (int) finalViewHolder.editTextHolder.getTag();
                        String viewHolderBalance = finalViewHolder.editTextHolder.getText().toString();
                        if (viewHolderBalance.isEmpty()) {
                            items.get(getPosition).setItemBal("0");
                        } else { items.get(getPosition).setItemBal(viewHolderBalance); }
                    }
                }
            });

            convertView.setTag(viewHolder);
            convertView.setTag(R.id.text_view_item_name, viewHolder.textViewHolder);
            convertView.setTag(R.id.text_view_item_description, viewHolder.editTextHolder);
            convertView.setTag(R.id.checkBox, viewHolder.checkboxHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.checkboxHolder.setTag(position); //This line is important
        viewHolder.editTextHolder.setTag(position);

        viewHolder.textViewHolder.setText(items.get(position).getItemName());
        viewHolder.editTextHolder.setText(items.get(position).getItemBal());
        viewHolder.checkboxHolder.setChecked(items.get(position).getItemState());

        // returns the view for the current row
        return convertView;
    }
}

