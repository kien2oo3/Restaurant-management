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
        database.close();
        return menuItems;
    }

    public ArrayList<MenuItems> getAllMenuItemByMenuDate(String date) {
        ArrayList<MenuItems> menuItems = new ArrayList<>();
        SQLiteDatabase database = databaseUtils.getReadableDatabase();
        String sql = "SELECT * FROM menu_items mi JOIN daily_menu_items dmi ON mi.menu_item_id = dmi.menu_item_id JOIN daily_menu dm ON dm.daily_menu_id = dmi.daily_menu_id WHERE dm.menu_date=?;";
        Cursor cursor = database.rawQuery(sql, new String[]{date});
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
        database.close();
        return menuItems;
    }

    public MenuItems getMenuItemByID(int id) {
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
        database.close();
        return item;
    }

    public long addNewMenuItem(MenuItems e) {
        SQLiteDatabase database = databaseUtils.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("menu_item_name", e.getMenu_item_name());
        values.put("menu_item_description", e.getMenu_item_description());
        values.put("menu_item_price", e.getMenu_item_price());
        values.put("image", e.getImage());
        long rs = database.insert("menu_items", null, values);
        database.close();
        return rs;
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
        int rs = database.update("menu_items", values, whereClause, whereArgs);
        database.close();
        return rs;
    }

    public int deleteMenuItemByID(int id) {
        SQLiteDatabase database = databaseUtils.getWritableDatabase();
        String whereClause = "menu_item_id=?";
        String[] whereArgs = new String[]{id + ""};
        int rs = database.delete("menu_items", whereClause, whereArgs);
        database.close();
        return rs;
    }

    public void close() {
        databaseUtils.close();
    }
}
