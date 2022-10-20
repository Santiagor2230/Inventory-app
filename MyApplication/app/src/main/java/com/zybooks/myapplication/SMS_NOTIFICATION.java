package com.zybooks.myapplication;

import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

public class SMS_NOTIFICATION {

    public static AlertDialog firstButton(final itemActivity context) {
        // Constructing a dialog for the notification
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Permission is needed")
                .setCancelable(false)
                .setMessage("The app requires permission to allow the user to be notified if the inventory is low ")
                .setPositiveButton("Enable", (dialog, arg1) -> {
                    Toast.makeText(context, "SMS Alerts Enable", Toast.LENGTH_LONG).show();
                    itemActivity.AllowSendSMS();
                    dialog.cancel();
                })
                .setNegativeButton("Disable", (dialog, arg1) -> {
                    Toast.makeText(context, "SMS Alerts Disable", Toast.LENGTH_LONG).show();
                    itemActivity.DenySendSMS();
                    dialog.cancel();
                });

        // Create the Alert and return it to item activity
        return builder.create();
    }

    public static AlertDialog secondButton(final itemActivity context) {
        // constructing the notifications properly
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("All items Delete")
                .setCancelable(false)
                .setMessage("Are you sure you want to delete all items ?")
                .setPositiveButton("yes", (dialog, arg1) -> {
                    itemActivity.YesDeleteItems();
                    dialog.cancel();
                })
                .setNegativeButton("No", (dialog, arg1) -> {
                    itemActivity.NoDeleteItems();
                    dialog.cancel();
                });

        // Create the alert and return it to item activity
        return builder.create();
    }
}
