package com.zybooks.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class itemsSQL extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "ItemsDatabase";
    private static final String TABLE_NAME = "ItemsTable";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_QUANTITY = "quantity";

    private static final String CREATE_ITEMS_TABLE = "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            COLUMN_NAME + " VARCHAR, " +
            COLUMN_DESCRIPTION + " VARCHAR, " +
            COLUMN_QUANTITY + " VARCHAR" + ");";

    public itemsSQL(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /*CRUD*/

    // Add item to database
    public void createItem(items item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME,item.getName());
        values.put(COLUMN_DESCRIPTION, item.getDesc());
        values.put(COLUMN_QUANTITY, item.getQty());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // Read item from Database
    public items readItem(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME,
                new String[] { COLUMN_ID, COLUMN_NAME, COLUMN_DESCRIPTION,  COLUMN_QUANTITY }, COLUMN_ID + " = ?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        items item = new items(Integer.parseInt(Objects.requireNonNull(cursor).getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3));

        cursor.close();

        return item;
    }

    // Update item in database
    public int updateItem(items item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, item.getName());
        values.put(COLUMN_DESCRIPTION, item.getDesc());
        values.put(COLUMN_QUANTITY, item.getQty());

        return db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[] { String.valueOf(item.getId()) });
    }

    // Delete item from database
    public void deleteItem(items item) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[] { String.valueOf(item.getId()) });
        db.close();
    }


    // Getting All Items
    public List<items> getAllItems() {
        List<items> itemList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                items item = new items();
                item.setId(Integer.parseInt(cursor.getString(0)));
                item.setName(cursor.getString(1));
                item.setDesc(cursor.getString(2));
                item.setQty(cursor.getString(3));

                itemList.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return itemList;
    }

    // Deleting all items
    public void deleteAllItems() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,null,null);
        db.close();
    }

    // Getting Items Count
    public int getItemsCount() {
        String countQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int itemsTotal = cursor.getCount();
        cursor.close();

        return itemsTotal;
    }
}
