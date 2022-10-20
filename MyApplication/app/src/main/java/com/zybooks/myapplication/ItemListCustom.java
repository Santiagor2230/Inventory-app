package com.zybooks.myapplication;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ItemListCustom extends BaseAdapter {

    //iniatilized variables
    private final Activity context;
    private PopupWindow updateDisplay;
    ArrayList<items> items;
    itemsSQL ItemsDB;

    //storing variables
    public ItemListCustom(Activity context, ArrayList<items> items, itemsSQL ItemsDB) {
        this.context = context;
        this.items = items;
        this.ItemsDB = ItemsDB;
    }

    //storing variables layout objects
    public static class ViewHolder {
        TextView newId;
        TextView newName;
        TextView newDescription;
        TextView newQuantity;
        ImageButton updateButton1;
        ImageButton deleteButton1;
    }

    //set up for the popup displayment and layout
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        ViewHolder newviewHolder;

        if (convertView == null) {
            newviewHolder = new ViewHolder();
            row = inflater.inflate(R.layout.item_design, null, true);

            newviewHolder.updateButton1 = row.findViewById(R.id.updateButton1);
            newviewHolder.newId = row.findViewById(R.id.newId);
            newviewHolder.newName = row.findViewById(R.id.newName);
            newviewHolder.newDescription = row.findViewById(R.id.newDescription);
            newviewHolder.newQuantity = row.findViewById(R.id.newQuantity);
            newviewHolder.deleteButton1 = row.findViewById(R.id.deleteButton1);

            row.setTag(newviewHolder);
        } else {
            newviewHolder = (ViewHolder) convertView.getTag();
        }

        //getting all the data
        newviewHolder.newId.setText("" + items.get(position).getId());
        newviewHolder.newName.setText(items.get(position).getName());
        newviewHolder.newDescription.setText(items.get(position).getDesc());
        newviewHolder.newQuantity.setText(items.get(position).getQty());


        final int positionPopup = position;

        newviewHolder.updateButton1.setOnClickListener(view -> editPopup(positionPopup));

        newviewHolder.deleteButton1.setOnClickListener(view -> {
            //Integer index = (Integer) view.getTag();
            ItemsDB.deleteItem(items.get(positionPopup));

            //items.remove(index.intValue());
            items = (ArrayList<items>) ItemsDB.getAllItems();
            notifyDataSetChanged();

            Toast.makeText(context, "Item Deleted", Toast.LENGTH_SHORT).show();

        });

        return  row;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public int getCount() {
        return items.size();
    }

    //layout of the popup when display in itemActivity
    public void editPopup(final int positionPopup) {
        LayoutInflater inflater = context.getLayoutInflater();
        View layout = inflater.inflate(R.layout.activity_update_items, context.findViewById(R.id.relativeLayout));

        updateDisplay = new PopupWindow(layout, 1100, 1200, true);
        updateDisplay.showAtLocation(layout, Gravity.CENTER, 0, 0);

        final EditText editItemName = layout.findViewById(R.id.updateName);
        final EditText editItemDesc = layout.findViewById(R.id.updateDescription);
        final EditText editItemQty = layout.findViewById(R.id.updateQuantity);

        editItemName.setText(items.get(positionPopup).getName());
        editItemDesc.setText(items.get(positionPopup).getDesc());
        editItemQty.setText(items.get(positionPopup).getQty());


        Button save = layout.findViewById(R.id.updateButton);
        Button cancel = layout.findViewById(R.id.cancelUpdate);

        save.setOnClickListener(view -> {

            String itemName = editItemName.getText().toString();
            String itemDesc = editItemDesc.getText().toString();
            String itemQty = editItemQty.getText().toString();


            items item = items.get(positionPopup);
            item.setName(itemName);
            item.setDesc(itemDesc);
            item.setQty(itemQty);


            ItemsDB.updateItem(item);
            items = (ArrayList<items>) ItemsDB.getAllItems();
            notifyDataSetChanged();

            Toast.makeText(context, "Item Updated", Toast.LENGTH_SHORT).show();

            updateDisplay.dismiss();
        });

        cancel.setOnClickListener(view -> {
            Toast.makeText(context, "Action Canceled", Toast.LENGTH_SHORT).show();
            updateDisplay.dismiss();
        });
    }
}
