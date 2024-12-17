package com.example.quanlynhahang.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanlynhahang.entity.DailyMenuItems;
import com.example.quanlynhahang.entity.MenuItems;
import com.example.quanlynhahang.utils.DatabaseUtils;

import java.util.ArrayList;

public class DailyMenuItemDAO {
    private DatabaseUtils databaseUtils;

    public DailyMenuItemDAO(Context context) {
        databaseUtils = new DatabaseUtils(context);
    }

    public ArrayList<DailyMenuItems> getAllDailyMenuItems() {
        ArrayList<DailyMenuItems> dailyMenuItems = new ArrayList<>();
        SQLiteDatabase database = databaseUtils.getReadableDatabase();
        String sql = "SELECT * FROM daily_menu_items";
        Cursor cursor = database.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                int daily_menu_item_id = cursor.getInt(cursor.getColumnIndexOrThrow("daily_menu_item_id"));
                int daily_menu_id = cursor.getInt(cursor.getColumnIndexOrThrow("daily_menu_id"));
                int menu_item_id = cursor.getInt(cursor.getColumnIndexOrThrow("menu_item_id"));
                DailyMenuItems d = new DailyMenuItems(daily_menu_item_id, daily_menu_id, menu_item_id);
                dailyMenuItems.add(d);
                cursor.moveToNext();
            }
        }
        cursor.close();

        return dailyMenuItems;
    }

    public boolean insertMenuItemsForDate(String date, ArrayList<MenuItems> list) {
        SQLiteDatabase database = databaseUtils.getWritableDatabase();

        long dailyMenuId = -1;
        boolean isSuccess = false;

        try {
            // Bắt đầu 1 giao dịch
            database.beginTransaction();

            // Chèn ngày vào bảng daily_menu
            ContentValues dailyMenuValues = new ContentValues();
            dailyMenuValues.put("menu_date", date);
            dailyMenuId = database.insert("daily_menu", null, dailyMenuValues);

            if (dailyMenuId == -1) {
                throw new Exception("Lỗi khi chèn vào bảng daily_menu!");
            }

            // Chèn danh sách món ăn vào bảng daily_menu_items
            for (MenuItems menuItem : list) {
                ContentValues menuItemValues = new ContentValues();
                menuItemValues.put("daily_menu_id", dailyMenuId);
                menuItemValues.put("menu_item_id", menuItem.getMenu_item_id());

                long result = database.insert("daily_menu_items", null, menuItemValues);
                if (result == -1) {
                    throw new Exception("Lỗi khi chèn vào bảng daily_menu_items");
                }
            }

            // Hoàn thành giao dịch
            isSuccess = true;
            database.setTransactionSuccessful();
        } catch (Exception e) {
            dailyMenuId = -1;
            e.printStackTrace();
        } finally {
            // Kết thúc giao dịch
            database.endTransaction();
            database.close();
        }
        return isSuccess;
    }

    public void close() {
        databaseUtils.close();
    }
}
