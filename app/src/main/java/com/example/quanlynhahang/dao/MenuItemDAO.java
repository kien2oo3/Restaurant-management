package com.example.quanlynhahang.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanlynhahang.entity.MenuItems;
import com.example.quanlynhahang.utils.DatabaseUtils;

import java.util.ArrayList;

public class MenuItemDAO {
    private DatabaseUtils databaseUtils;

    public MenuItemDAO(Context context) {
        databaseUtils = new DatabaseUtils(context);
    }

    public ArrayList<MenuItems> getAllMenuItem() {
        ArrayList<MenuItems> menuItems = new ArrayList<>();
        SQLiteDatabase database = databaseUtils.getReadableDatabase();
        String sql = "SELECT * FROM menu_items";
        Cursor cursor = database.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                int menu_item_id = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("menu_item_id")));
                String menu_item_name = cursor.getString(cursor.getColumnIndexOrThrow("menu_item_name"));
                String menu_item_description = cursor.getString(cursor.getColumnIndexOrThrow("menu_item_description"));
                int menu_item_price = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("menu_item_price")));
                String image = cursor.getString(cursor.getColumnIndexOrThrow("image"));
                MenuItems x = new MenuItems(menu_item_id, menu_item_name, menu_item_description, menu_item_price, image);
                menuItems.add(x);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return menuItems;
    }

    public MenuItems getAllMenuItemByID(int id) {
        MenuItems item = null;
        SQLiteDatabase database = databaseUtils.getReadableDatabase();
        String sql = "SELECT * FROM menu_items WHERE menu_item_id=?";
        Cursor cursor = database.rawQuery(sql, new String[]{id + ""});
        if (cursor.moveToFirst()) {
            int menu_item_id = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("menu_item_id")));
            String menu_item_name = cursor.getString(cursor.getColumnIndexOrThrow("menu_item_name"));
            String menu_item_description = cursor.getString(cursor.getColumnIndexOrThrow("menu_item_description"));
            int menu_item_price = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("menu_item_price")));
            String image = cursor.getString(cursor.getColumnIndexOrThrow("image"));
            item = new MenuItems(menu_item_id, menu_item_name, menu_item_description, menu_item_price, image);
        }

        cursor.close();
        return item;
    }

    public long addNewMenuItem(MenuItems e) {
        SQLiteDatabase database = databaseUtils.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("menu_item_name", e.getMenu_item_name());
        values.put("menu_item_description", e.getMenu_item_description());
        values.put("menu_item_price", e.getMenu_item_price());
        values.put("image", e.getImage());
        return database.insert("menu_items", null, values);
    }

    public int editMenuItemByID(MenuItems e) {
        SQLiteDatabase database = databaseUtils.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("menu_item_name", e.getMenu_item_name());
        values.put("menu_item_description", e.getMenu_item_description());
        values.put("menu_item_price", e.getMenu_item_price());
        values.put("image", e.getImage());
        String whereClause = "menu_item_id=?";
        String[] whereArgs = new String[]{e.getMenu_item_id() + ""};
        return database.update("menu_items", values, whereClause, whereArgs);
    }

    public int deleteMenuItemByID(int id) {
        SQLiteDatabase database = databaseUtils.getWritableDatabase();
        String whereClause = "menu_item_id=?";
        String[] whereArgs = new String[]{id + ""};
        return database.delete("menu_items", whereClause, whereArgs);
    }

    public void close() {
        databaseUtils.close();
    }
}