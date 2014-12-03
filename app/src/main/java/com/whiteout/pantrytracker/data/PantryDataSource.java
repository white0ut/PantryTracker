package com.whiteout.pantrytracker.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.whiteout.pantrytracker.data.model.Item;
import com.whiteout.pantrytracker.data.model.Recipe;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Author:  Kendrick Cline
 * Date:    12/2/14
 * Email:   kdecline@gmail.com
 */
public class PantryDataSource {

    private SQLiteDatabase database;
    private PantrySQLiteHelper dbHelper;

    private String[] allItemColumns = {
            PantrySQLiteHelper.KEY_ID,
            PantrySQLiteHelper.KEY_ITEM_NAME,
            PantrySQLiteHelper.KEY_EXPIRATION,
            PantrySQLiteHelper.KEY_QUANTITY,
            PantrySQLiteHelper.KEY_UNIT };

    private String[] allRecipeColumns = {
            PantrySQLiteHelper.KEY_ID,
            PantrySQLiteHelper.KEY_YUMMLY_ID,
            PantrySQLiteHelper.KEY_DIRECTIONS,
            PantrySQLiteHelper.KEY_YIELD,
            PantrySQLiteHelper.KEY_RECIPE_NAME
    };

    private String[] allItemRecipeColumns = {
            PantrySQLiteHelper.KEY_ID,
            PantrySQLiteHelper.KEY_ITEM_ID,
            PantrySQLiteHelper.KEY_RECIPE_ID
    };

    private static final String TAG = "Pantry";

    public PantryDataSource(Context context) {
        dbHelper = new PantrySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void deleteItem(Item item) {
        long id = item.getId();
        Log.d(TAG, "delete item = " + id);
        database.delete(PantrySQLiteHelper.TABLE_ITEM, PantrySQLiteHelper.KEY_ID
        + " = " + id, null);
    }

    public void deleteItem(Float id) {
        Log.d(TAG, "delete item = " + id);
        database.delete(PantrySQLiteHelper.TABLE_ITEM, PantrySQLiteHelper.KEY_ID
                + " = " + id, null);
    }

    public void deleteAllItems() {
        Log.d(TAG, "delete all items... ");
        database.delete(PantrySQLiteHelper.TABLE_ITEM, null, null);
    }

    public void editItem(Item item) {
        ContentValues values = new ContentValues();
        values.put(PantrySQLiteHelper.KEY_ITEM_NAME, item.getName());
        values.put(PantrySQLiteHelper.KEY_EXPIRATION, item.getExpiration());
        values.put(PantrySQLiteHelper.KEY_QUANTITY, item.getQuantity());
        values.put(PantrySQLiteHelper.KEY_UNIT, item.getUnit());

        int newId = database.update(PantrySQLiteHelper.TABLE_ITEM, values, PantrySQLiteHelper.KEY_ID + " = " + item.getId(), null);


//        Cursor cursor = database.query(PantrySQLiteHelper.TABLE_ITEM,
//                allItemColumns, PantrySQLiteHelper.KEY_ID + " = " + newId, null,
//        null, null, null);
//        cursor.moveToFirst();
//
//        Item newItem = cursorToItem(cursor);
//        cursor.close();
//
//        return newItem;
    }

    public Item addItem(Item item) {
        ContentValues values = new ContentValues();
        values.put(PantrySQLiteHelper.KEY_ITEM_NAME, item.getName());
        values.put(PantrySQLiteHelper.KEY_EXPIRATION, item.getExpiration());
        values.put(PantrySQLiteHelper.KEY_QUANTITY, item.getQuantity());
        values.put(PantrySQLiteHelper.KEY_UNIT, item.getUnit());
        long insertId = database.insert(PantrySQLiteHelper.TABLE_ITEM,
                null, values);

        Cursor cursor = database.query(PantrySQLiteHelper.TABLE_ITEM,
                allItemColumns,PantrySQLiteHelper.KEY_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();

        Item newItem = cursorToItem(cursor);

        // Log the item stored
        Log.d(TAG, "item = " + newItem.toString() + " insert ID = " + insertId);

        cursor.close();
        return newItem;
    }

    public Recipe addRecipe(Recipe recipe) {
        ContentValues values = new ContentValues();
        values.put(PantrySQLiteHelper.KEY_YUMMLY_ID, recipe.getYummlyId());
        values.put(PantrySQLiteHelper.KEY_DIRECTIONS, recipe.getDirections());
        values.put(PantrySQLiteHelper.KEY_YIELD, recipe.getYield());
        values.put(PantrySQLiteHelper.KEY_RECIPE_NAME, recipe.getName());
        long insertId = database.insert(PantrySQLiteHelper.TABLE_RECIPE,
                null, values);
        if (insertId == -1) {
            return null;
        }
        recipe.setId(insertId);
        return recipe;
    }

    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<Item>();
        Log.d(TAG, "Fetching Items");

        Cursor cursor = database.query(PantrySQLiteHelper.TABLE_ITEM,
                allItemColumns, null, null,null,null, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Item item = cursorToItem(cursor);
            Log.d(TAG, "get item = " + cursorToItem(cursor).toString());
            items.add(item);
            cursor.moveToNext();
        }
        // close that cursor
        cursor.close();
        return items;
    }

    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<Recipe>();
        Log.d(TAG, "Fetching recipes");

        Cursor cursor = database.query(PantrySQLiteHelper.TABLE_RECIPE,
                allRecipeColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Recipe rec = cursorToRecipe(cursor);
            recipes.add(rec);
            cursor.moveToNext();
        }
        cursor.close();
        return recipes;
    }

    private Recipe cursorToRecipe(Cursor cursor) {
        Recipe rec = new Recipe();
        rec.setId(cursor.getLong(0));
        rec.setYummlyId(cursor.getString(1));
        rec.setdirections(cursor.getString(2));
        rec.setYield(cursor.getString(3));
        rec.setName(cursor.getString(4));
        return rec;
    }

    private Item cursorToItem(Cursor cursor) {
        Item item = new Item();
        item.setId(cursor.getLong(0));
        item.setName(cursor.getString(1));
        item.setExpiration(cursor.getLong(2));
        item.setQuantity(cursor.getFloat(3));
        item.setUnit(cursor.getString(4));
        return item;
    }


}
