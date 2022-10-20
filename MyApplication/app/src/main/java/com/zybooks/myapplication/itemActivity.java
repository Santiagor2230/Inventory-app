package com.zybooks.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class itemActivity extends AppCompatActivity {

    //initializing variables
    ImageButton BUTTON_ADD, SMS_BUTTON, DELETE_ALL_BUTTON;
    ListView listOfItems;
    itemsSQL itemsDB;
    AlertDialog alerts = null;
    ItemListCustom customList;
    ArrayList<items> items;
    int itemsCount;

    //initializing the notifications
    private static final int USER_PERMISSION_REQUEST_SEND_SMS = 0;
    private static boolean smsAuthorized = false;
    private static boolean deleteItems = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        //initializing the layout objects
        BUTTON_ADD = findViewById(R.id.addbutton1);
        SMS_BUTTON = findViewById(R.id.notificationbutton);
        DELETE_ALL_BUTTON = findViewById(R.id.deleteallButton);
        listOfItems = findViewById(R.id.insertItems);

        //using the items database
        itemsDB = new itemsSQL(this);

        //creating an array list from the items database
        items = (ArrayList<items>) itemsDB.getAllItems();

        itemsCount = itemsDB.getItemsCount();

        //amount of the item
        if(itemsCount > 0){
            customList = new ItemListCustom(this, items, itemsDB);
            listOfItems.setAdapter(customList);
        }
        else{
            Toast.makeText(this,"This database is empty", Toast.LENGTH_LONG).show();

        }

        //add takes us to the add items activity in order to add new items
        BUTTON_ADD.setOnClickListener(view -> {
            Intent add = new Intent(this, addItemsActivity.class);
            startActivityForResult(add,1);

        });

        //set up the sms notification
        SMS_BUTTON.setOnClickListener(view -> {

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED){

                if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.SEND_SMS)){
                    Toast.makeText(this, "SMS Permission is Needed", Toast.LENGTH_LONG).show();

                }

                else{
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},USER_PERMISSION_REQUEST_SEND_SMS);

                }
            }
            else{
                Toast.makeText(this,"This Device has SMS permission", Toast.LENGTH_LONG).show();

            }

            alerts = SMS_NOTIFICATION.firstButton(this);
            alerts.show();
        });

        //delete notifications
        DELETE_ALL_BUTTON.setOnClickListener(view -> {
            itemsCount = itemsDB.getItemsCount();

            if(itemsCount > 0) {

                alerts = SMS_NOTIFICATION.secondButton(this);
                alerts.show();

                alerts.setCancelable(true);
                alerts.setOnCancelListener(dialog -> DeleteAllItems());
            }
            else{
                Toast.makeText(this,"Database is Empty",Toast.LENGTH_LONG).show();

            }
        });

    }


    //returns the values of add items and reads them in the list
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                itemsCount = itemsDB.getItemsCount();

                if(customList == null)	{
                    customList = new ItemListCustom(this, items, itemsDB);
                    listOfItems.setAdapter(customList);
                }

                customList.items = (ArrayList<items>) itemsDB.getAllItems();
                ((BaseAdapter)listOfItems.getAdapter()).notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Action Canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }




    // Receive and evaluate user response from AlertDialog to delete items
    public static void YesDeleteItems() {
        deleteItems = true;
    }

    public static void NoDeleteItems() {
        deleteItems = false;
    }

    public void DeleteAllItems() {
        if (deleteItems) {
            itemsDB.deleteAllItems();
            Toast.makeText(this, "All Items were Deleted", Toast.LENGTH_SHORT).show();


            if (customList == null) {
                customList = new ItemListCustom(this,items,itemsDB);
                listOfItems.setAdapter(customList);
            }

            customList.items = (ArrayList<items>) itemsDB.getAllItems();
            ((BaseAdapter) listOfItems.getAdapter()).notifyDataSetChanged();
        }
    }

    // Receive and evaluate user response from AlertDialog to send SMS notifications
    public static void AllowSendSMS() {
        smsAuthorized = true;
    }

    public static void DenySendSMS() {
        smsAuthorized = false;
    }

    public static void SendSMSMessage(Context context) {

        // Evaluate AlertDialog permission to send SMS and checks the items amount
        if (smsAuthorized) {
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(null, null, "you have zero items", null, null);
                Toast.makeText(context, "SMS Sent", Toast.LENGTH_LONG).show();
            } catch (Exception ex) {
                Toast.makeText(context, "Device Permission Denied", Toast.LENGTH_LONG).show();
                ex.printStackTrace();
            }
        } else {
            Toast.makeText(context, "App SMS Alert Disable", Toast.LENGTH_LONG).show();
        }
    }
}