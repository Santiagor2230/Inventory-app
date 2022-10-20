package com.zybooks.myapplication;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button LOGIN_BUTTON, CREATE_BUTTON;
    EditText Username, Password;
    SQLiteDatabase db;
    UsersSQL usersCredentials;
    Boolean statusEmpty;
    String CheckPassword = "No Password";
    String namePlacement, usernamePlacement, passwordPlacement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //variables for each functional text and button
        LOGIN_BUTTON = findViewById(R.id.buttonLogin);
        CREATE_BUTTON = findViewById(R.id.buttonCreate);
        Username = findViewById(R.id.usernameEditText);
        Password = findViewById(R.id.passwordEditText);
        usersCredentials = new UsersSQL(this);

        //if the log in button is click
        LOGIN_BUTTON.setOnClickListener(view -> {
            //we get into user login
            userLogin();
        });

        //if the create button is click
        CREATE_BUTTON.setOnClickListener(view -> {
            //we are send to the register class
            Intent intent = new Intent(MainActivity.this, register.class);
            startActivity(intent);
        });
    }
    //login
    public void userLogin(){
        //getting username and password
        usernamePlacement = Username.getText().toString().trim();
        passwordPlacement = Password.getText().toString().trim();

        //checking to see if username or password is empty
        if(usernamePlacement.isEmpty()){
            Username.requestFocus();
            statusEmpty = true;
        }
        else if (passwordPlacement.isEmpty()){
            Password.requestFocus();
            statusEmpty = true;
        }
        else {
            statusEmpty = false;
        }

        //if the text are not empty
        if(!statusEmpty){

            //opening database with write permission
            db = usersCredentials.getWritableDatabase();

            //checking to see username
            Cursor cursor = db.query(UsersSQL.TABLE_NAME, null, " " + UsersSQL.COLUMN_USERNAME + "=?", new String[]{usernamePlacement}, null, null, null);

            //based on the username we get to check the other credentials that come with this user
            while(cursor.moveToNext()){
                if(cursor.isFirst()){
                    cursor.moveToFirst();

                    //querying to obtain the passwords value and users name
                    CheckPassword = cursor.getString(cursor.getColumnIndexOrThrow(UsersSQL.COLUMN_PASSWORD));
                    namePlacement = cursor.getString(cursor.getColumnIndexOrThrow(UsersSQL.COLUMN_NAME));

                    //we close this cursor
                    cursor.close();

                }
            }
            //we close the query
            usersCredentials.close();

            //we check credentials
            passwordCredentials();

        }

    }

    //check to see if password credentials match
    public void passwordCredentials(){
        if(CheckPassword.equalsIgnoreCase(passwordPlacement)){
            //display when user is able to enter
            Toast.makeText(MainActivity.this, "Successful Login", Toast.LENGTH_SHORT).show();

            /*Bundle bundle = new Bundle();
            bundle.putString("name", namePlacement);
            bundle.putString("userName", usernamePlacement);*/

            //transitioning into the items UI
            Intent intent = new Intent(MainActivity.this, itemActivity.class);
            startActivity(intent);

            //empty text after log in
            Username.getText().clear();
            Password.getText().clear();


        }
        //show a text with incorrect credentials
        else{

            Toast.makeText(MainActivity.this, "Incorrect Log in credentials please try again or create a new account",Toast.LENGTH_LONG).show();
        }
    }
}