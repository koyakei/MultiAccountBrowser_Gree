package com.koyakei.MUB;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "testdb";

    private static final String CREATE_TABLE = "CREATE TABLE emp ("
            + "key INTEGER PRIMARY KEY AUTOINCREMENT," + "name TEXT," + "url TEXT," + "cookie TEXT" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    	//Log.d("このアプリ","テーブルの内容");
        db.execSQL(CREATE_TABLE);

        //insert(db, "Google", "http://www.google.co.jp", "");
        //insert(db, "Yahoo", "http://www.yahoo.jp", "");
        for (int i = 1; i <= 50; i++) {
        	insert(db, "Gree"+i, "http://gree.jp", "");
        }
        //insert(db, "mixi1", "http://mixi.jp", "");
        //insert(db, "mixi2", "http://mixi.jp", "");
        //insert(db, "mixi3", "http://mixi.jp", "");
        //insert(db, "mixi4", "http://mixi.jp", "");
        //insert(db, "mixi5", "http://mixi.jp", "");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS emp");
        onCreate(db);
    }

    public void insert(SQLiteDatabase db, String name, String url, String cookie) {
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("url", url);
        cv.put("cookie", cookie);
        db.insert("emp", "", cv);
    }

    public void update(SQLiteDatabase db, int key, String cookie) {
        ContentValues cv = new ContentValues();
        cv.put("cookie", cookie);
        db.update("emp", cv, "key = " + key, null);
    }

}
