package com.example.acollectersinventory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.CursorAdapter;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SqlHelper extends SQLiteOpenHelper {
    public static final int DB_VERSION = 6;

    public static final String DATABASE_NAME = "register.db";
    public static final String TABLE_NAME = "User";
    public static final String ID = "ID";
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";

    public static final String INVENTORY_TABLE = "Inventory";
    public static final String _ID= "INVENTORY_NR";
    public static final String TITLE = "title";
    public static final String CATEGORY = "category";
    public static final String DESCRIPTION = "description";
    CursorAdapter adapter;


    public SqlHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE User (ID INTEGER PRIMARY KEY AUTOINCREMENT, Username TEXT, Email TEXT UNIQUE, password TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE Inventory (_id INTEGER PRIMARY KEY AUTOINCREMENT, Title TEXT, Category TEXT, Description TEXT,Email TEXT, FOREIGN KEY(Email) REFERENCES User)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+INVENTORY_TABLE);
        onCreate(db);

    }


    public int addUser(String username, String email, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("email", email);
        contentValues.put("password", password);
        int res = (int) db.insert("User", null, contentValues);
        db.close();
        return res;
    }

    public int addToInventory(String title, String category, String description, String email){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Title", title);
        cv.put("Category", category);
        cv.put("Description", description);
        cv.put("Email", email);
        int res = (int) db.insert("Inventory", null, cv);
        db.close();
        return res;
    }

    public boolean checkUser(String email, String password){
        String[] columns = {EMAIL};
        SQLiteDatabase db = getReadableDatabase();
        String selection = EMAIL + "=?" + " AND " + PASSWORD + "=?";
        String[] selectionArgs = {email, password};
        Cursor cursor = db.query("User", columns, selection,selectionArgs,null,null,null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        if(count > 0){
            return true;
        }else{
            return false;
        }
    }

    public boolean checkEmail(String email){
        String[] columns = {EMAIL};
        SQLiteDatabase db = getReadableDatabase();
        String selection = EMAIL + "=?";
        String[] selectionArgs ={email};
        Cursor cursor = db.query("User", columns, selection, selectionArgs, null,null,null);
        int count = cursor.getCount();
        db.close();

        if(count == 0){
            return false;
        }else{
            return true;
        }
    }

    public Cursor getID(String id){
        String[] args = {id};
        return (getReadableDatabase().rawQuery("SELECT _id FROM Inventory WHERE _id=?", args));
    }
}
