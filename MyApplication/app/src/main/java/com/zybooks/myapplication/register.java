package com.zybooks.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class register extends AppCompatActivity {

    //initializing variables
    Button CREATE_BUTTON, RETRY_BUTTON;
    EditText nameText, usernameText, passwordText;
    Boolean statusEmpty, registerStatus;
    UsersSQL usersCredentials;
    SQLiteDatabase db;
    String name, userName, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //initializing the layout objects
        CREATE_BUTTON = findViewById(R.id.createButton2);
        RETRY_BUTTON = findViewById(R.id.retryButton);
        nameText = findViewById(R.id.nameEditText);
        usernameText = findViewById(R.id.usernameEditText2);
        passwordText = findViewById(R.id.passwordEditText2);
        usersCredentials = new UsersSQL(this);

        //when using the create button
        CREATE_BUTTON.setOnClickListener(view -> {
            userRegistration();
        });

        //when using the retry button
        RETRY_BUTTON.setOnClickListener(view -> {
            startActivity(new Intent(register.this, MainActivity.class));
            this.finish();
        });
    }

    //registering a user process
    public void userRegistration(){

        statusEmpty = CheckIfEmpty();
        if(!statusEmpty) {

            registerStatus = CheckIfUsernameExist();

            if(!registerStatus) {

                name = nameText.getText().toString();
                userName = usernameText.getText().toString();
                pass = passwordText.getText().toString();

                Users user = new Users(name, userName, pass);
                usersCredentials.createUser(user);

                Toast.makeText(register.this, "You are now register", Toast.LENGTH_LONG).show();

                startActivity(new Intent(register.this, MainActivity.class));
                this.finish();
            }
            else{
                Toast.makeText(register.this ,"Email already in database",Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(register.this,"Some areas are not filled",Toast.LENGTH_LONG).show();
        }

    }

    //checks to see if all areas are empty or not
    public boolean CheckIfEmpty(){
        String name = nameText.getText().toString();
        String userName = usernameText.getText().toString();
        String pass = passwordText.getText().toString();
        if(name.isEmpty() || userName.isEmpty() || pass.isEmpty()){
            statusEmpty = true;
        }
        else{
            statusEmpty = false;
        }
        return statusEmpty;
    }

    //checks to see if the username is already in the database
    public boolean CheckIfUsernameExist(){

        String tempUsername = usernameText.getText().toString().trim();
        db = usersCredentials.getWritableDatabase();
        registerStatus = false;

        Cursor cursor = db.query(UsersSQL.TABLE_NAME,null,""+ UsersSQL.COLUMN_USERNAME + "=?", new String[]{tempUsername}, null, null, null);

        while(cursor.moveToNext()){
            if(cursor.isFirst()){
                cursor.moveToFirst();

                registerStatus = true;

                cursor.close();
            }

        }

        usersCredentials.close();
        return registerStatus;


    }
}