package com.whiteout.pantrytracker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Author:  Kendrick Cline
 * Date:    12/2/14
 * Email:   kdecline@gmail.com
 */
public class PantrySQLiteHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "pantry";

    public static final String TABLE_ITEM = "item";
    public static final String KEY_ID = "id";
    public static final String KEY_ITEM_NAME = "name";
    public static final String KEY_QUANTITY = "quantity";
    public static final String KEY_EXPIRATION = "expiration";
    public static final String KEY_UNIT = "unit";

    public static final String TABLE_ITEM_RECIPE = "item_recipe";
    public static final String KEY_ITEM_ID = "item_id";
    public static final String KEY_RECIPE_ID = "recipe_id";

    public static final String TABLE_RECIPE = "recipe";
    public static final String KEY_RECIPE_NAME = "name";
    public static final String KEY_DIRECTIONS = "directions";


    // Database creation SQL statement
    private static final String CREATE_TABLE_ITEM = "CREATE TABLE IF NOT EXISTS "
            + TABLE_ITEM
            + " ("
            + KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_ITEM_NAME
            + " TEXT NOT NULL, "
            + KEY_QUANTITY
            + " FLOAT NOT NULL DEFAULT 0, "
            + KEY_EXPIRATION
            + " LONG, "
            + KEY_UNIT
            + " TEXT);";

    private static final String CREATE_TABLE_RECIPE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_RECIPE
            + " ("
            + KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_DIRECTIONS
            + " TEXT, "
            + KEY_RECIPE_NAME
            + " TEXT NOT NULL);";

    private static final String CREATE_TABLE_ITEM_RECIPE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_ITEM_RECIPE
            + " ("
            + KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_ITEM_ID
            + " INTEGER NOT NULL, "
            + KEY_RECIPE_ID
            + " INTEGER NOT NULL, "
            + " FOREIGN KEY(" + KEY_ITEM_ID + ") REFERENCES " + TABLE_ITEM + "(" + KEY_ID + "), "
            + " FOREIGN KEY(" + KEY_RECIPE_ID + ") REFERENCES " + TABLE_RECIPE + "(" + KEY_ID + "));";

    private Context mContext;

    public PantrySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ITEM);
        db.execSQL(CREATE_TABLE_RECIPE);
        db.execSQL(CREATE_TABLE_ITEM_RECIPE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
