package com.example.quanlynhahang.dao;

import android.content.ContentValues;
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

    public int getDailyMenuIdByDate(String date){
        SQLiteDatabase database = databaseUtils.getReadableDatabase();
        int id = -1;
        String sql = "SELECT daily_menu_id FROM daily_menu WHERE menu_date=?";
        Cursor cursor = database.rawQuery(sql, new String[]{date});
        if(cursor.moveToFirst()){
            id = cursor.getInt(0);
        }
        return id;
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

    public boolean updateMenuItemsForDate(String date, ArrayList<MenuItems> newMenuItemsList) {
        SQLiteDatabase database = databaseUtils.getWritableDatabase();
        boolean isSuccess = false;
        try {
            // Bắt đầu transaction để đảm bảo tính toàn vẹn dữ liệu
            database.beginTransaction();

            // Lấy id cua date
            int id = getDailyMenuIdByDate(date);
            if(id==-1){
                return false;
            }

            // Xóa các menu_item liên quan đến ngày
            database.delete(
                    "daily_menu_items",
                    "daily_menu_id IN (SELECT daily_menu_id FROM daily_menu WHERE menu_date=?)",
                    new String[]{date}
            );

            // Thêm các menu_item mới
            // Chèn danh sách món ăn vào bảng daily_menu_items
            for (MenuItems menuItem : newMenuItemsList) {
                ContentValues menuItemValues = new ContentValues();
                menuItemValues.put("daily_menu_id", id);
                menuItemValues.put("menu_item_id", menuItem.getMenu_item_id());

                long result = database.insert("daily_menu_items", null, menuItemValues);
                if (result == -1) {
                    throw new Exception("Lỗi khi chèn vào bảng daily_menu_items");
                }
            }

            // Hoàn thành transaction
            database.setTransactionSuccessful();
            isSuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.endTransaction(); // Kết thúc transaction
            database.close();
        }
        return isSuccess;
    }


    public boolean deleteMenuItemsForDate(String date) {
        SQLiteDatabase database = databaseUtils.getWritableDatabase();
        boolean isSuccess = false;
        try {
            database.beginTransaction();
            // Xóa từ bảng daily_menu_items trước:
            database.delete("daily_menu_items", "daily_menu_id IN (SELECT daily_menu_id FROM daily_menu WHERE menu_date=?)", new String[]{date});

            // Tiếp theo là xóa đến bảng daily_menu
            database.delete("daily_menu", "menu_date=?", new String[]{date});

            database.setTransactionSuccessful();

            isSuccess = true;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
            database.close();
        }
        return isSuccess;
    }

    public void close() {
        databaseUtils.close();
    }
}
