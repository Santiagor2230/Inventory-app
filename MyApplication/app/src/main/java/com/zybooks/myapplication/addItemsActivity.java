package com.zybooks.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.atomic.AtomicReference;

public class addItemsActivity extends AppCompatActivity {

    //setting up variables
    Button cancelButton, addItem;
    EditText itemName, itemDescription, itemQuantity;
    Boolean emptyStatus;
    itemsSQL itemsDB;
    String nameHolder, descHolder, qtyHolder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_items);

        //initializing the variables to the layout objects
        itemName = findViewById(R.id.addName);
        itemDescription = findViewById(R.id.addDescription);
        itemQuantity = findViewById(R.id.addQuantity);
        cancelButton = findViewById(R.id.cancelAdd);
        addItem = findViewById(R.id.addButton);

        //initializing the database for items
        itemsDB = new itemsSQL(this);

        AtomicReference<Intent> intent = new AtomicReference<>(getIntent());

        //cancel button functionality
        cancelButton.setOnClickListener(view -> {

            Intent add = new Intent();
            setResult(0, add);
            this.finish();
        });

        //add items button functionality
        addItem.setOnClickListener(view -> NewInstanceInList());
    }

        //new instances for the list
        public void NewInstanceInList(){
            nameHolder = itemName.getText().toString().trim();
            descHolder = itemDescription.getText().toString().trim();
            qtyHolder = itemQuantity.getText().toString().trim();

            if(nameHolder.isEmpty() || descHolder.isEmpty() || qtyHolder.isEmpty())
            {
                emptyStatus = true;
            }
            else{
                emptyStatus = false;
            }
            if(!emptyStatus){
                String name = nameHolder;
                String description = descHolder;
                String quantity = qtyHolder;

                items item = new items(name,description,quantity);
                itemsDB.createItem(item);

                Toast.makeText(this,"Item has been added succesfully", Toast.LENGTH_LONG).show();

                Intent add = new Intent();
                setResult(RESULT_OK, add);
                this.finish();
            }
            else{
                Toast.makeText(this, "it is Empty", Toast.LENGTH_LONG).show();
            }
        }
}