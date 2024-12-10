package com.example.quanlynhahang.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanlynhahang.entity.DailyMenu;
import com.example.quanlynhahang.entity.MenuItems;
import com.example.quanlynhahang.utils.DatabaseUtils;

import java.util.ArrayList;

public class DailyMenuDAO {
    private DatabaseUtils databaseUtils;

    public DailyMenuDAO(Context context) {
        databaseUtils = new DatabaseUtils(context);
    }

    public ArrayList<DailyMenu> getAllDailyMenu() {
        ArrayList<DailyMenu> dailyMenus = new ArrayList<>();
        SQLiteDatabase database = databaseUtils.getReadableDatabase();
        String sql = "SELECT * FROM daily_menu";
        Cursor cursor = database.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                int daily_menu_id = cursor.getInt(cursor.getColumnIndexOrThrow("daily_menu_id"));
                String menu_date = cursor.getString(cursor.getColumnIndexOrThrow("menu_date"));
                DailyMenu dailyMenu = new DailyMenu(daily_menu_id, menu_date);
                dailyMenus.add(dailyMenu);
                cursor.moveToNext();
            }
        }
        cursor.close();
        database.close();
        return dailyMenus;
    }

    public int getTotalItemByDate(String date) {
        SQLiteDatabase database = databaseUtils.getReadableDatabase();
        int total = 0;
        String sql = "SELECT \n" +
                "    COUNT(dmi.menu_item_id) AS total_items\n" +
                "FROM \n" +
                "    daily_menu dm\n" +
                "JOIN \n" +
                "    daily_menu_items dmi\n" +
                "ON \n" +
                "    dm.daily_menu_id = dmi.daily_menu_id\n" +
                "WHERE \n" +
                "    dm.menu_date = ?\n" +
                "GROUP BY \n" +
                "    dm.menu_date;\n";
        Cursor cursor = database.rawQuery(sql, new String[]{date});
        if (cursor.moveToFirst()) {
            total = cursor.getInt(0);
        }
        cursor.close();
        database.close();
        return total;
    }


    public ArrayList<MenuItems> getItemsIDByDate(String date) {
        SQLiteDatabase database = databaseUtils.getReadableDatabase();
        ArrayList<MenuItems> listItemsByDate = new ArrayList<>();
        String sql = "SELECT \n" +
                "    mi.menu_item_id, \n" +
                "    mi.menu_item_name, \n" +
                "    mi.menu_item_description, \n" +
                "    mi.menu_item_price, \n" +
                "    mi.image \n" +
                "FROM \n" +
                "    daily_menu dm\n" +
                "JOIN \n" +
                "    daily_menu_items dmi\n" +
                "ON \n" +
                "    dm.daily_menu_id = dmi.daily_menu_id\n" +
                "JOIN \n" +
                "    menu_items mi\n" +
                "ON \n" +
                "    dmi.menu_item_id = mi.menu_item_id\n" +
                "WHERE \n" +
                "    dm.menu_date = ?;";
        Cursor cursor = database.rawQuery(sql, new String[]{date});
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("menu_item_id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("menu_item_name"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("menu_item_description"));
                int price = cursor.getInt(cursor.getColumnIndexOrThrow("menu_item_price"));
                String img = cursor.getString(cursor.getColumnIndexOrThrow("image"));
                listItemsByDate.add(new MenuItems(id, name, description, price, img));
                cursor.moveToNext();
            }
        }
        cursor.close();
        database.close();
        return listItemsByDate;
    }

    public void close() {
        databaseUtils.close();
    }
}
