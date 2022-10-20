package com.zybooks.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UsersSQL extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "Users Database";
    public static final String TABLE_NAME = "UsersTable";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";

    private static final String CREATE_USERS_TABLE = "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            COLUMN_NAME + " VARCHAR, " +
            COLUMN_USERNAME + " VARCHAR, " +
            COLUMN_PASSWORD + " VARCHAR" + ");";

    public UsersSQL(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

   /* Building the Crud
   * */

    // Add user to database
    public void createUser(Users user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, user.getUserName());
        values.put(COLUMN_USERNAME, user.getUserUsername());
        values.put(COLUMN_PASSWORD, user.getUserPass());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // Read user from Database
    public Users readUser(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME,
                new String[] { COLUMN_ID, COLUMN_NAME, COLUMN_USERNAME, COLUMN_PASSWORD }, COLUMN_ID + " = ?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Users user = new Users(Integer.parseInt(Objects.requireNonNull(cursor).getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3));

        cursor.close();

        return user;
    }

    // Update user in database
    public int updateUser(Users user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, user.getUserName());
        values.put(COLUMN_USERNAME, user.getUserUsername());
        values.put(COLUMN_PASSWORD, user.getUserPass());

        return db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[] { String.valueOf(user.getId()) });
    }

    // Delete user from database
    public void deleteItem(Users user) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[] { String.valueOf(user.getId()) });
        db.close();
    }

    // Get All Users from database
    public List<Users> getAllUsers() {
        List<Users> userList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding users to list
        if (cursor.moveToFirst()) {
            do {
                Users user = new Users();
                user.setId(Integer.parseInt(cursor.getString(0)));
                user.setUserName(cursor.getString(1));
                user.setUserUsername(cursor.getString(2));
                user.setUserPass(cursor.getString(3));

                userList.add(user);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return userList;
    }

    // Deleting All Users
    public void deleteAllUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,null,null);
        db.close();
    }

    // Getting Users Count
    public int getUsersCount() {
        String countQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int usersTotal = cursor.getCount();
        cursor.close();

        return usersTotal;
    }
}
