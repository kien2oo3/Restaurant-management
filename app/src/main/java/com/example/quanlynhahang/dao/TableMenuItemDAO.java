package com.example.quanlynhahang.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.quanlynhahang.entity.MenuItems;
import com.example.quanlynhahang.entity.TableMenuItems;
import com.example.quanlynhahang.utils.DatabaseUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

public class TableMenuItemDAO {
    private DatabaseUtils databaseUtils;

    public TableMenuItemDAO(Context context) {
        databaseUtils = new DatabaseUtils(context);
    }

    public int getTotalBySelectedNewItems(Map<MenuItems, Integer> listSelected){
        int rs = 0;
        for( Map.Entry<MenuItems, Integer> entry : listSelected.entrySet() ){
            rs += entry.getValue() * entry.getKey().getMenu_item_price();

        }
        return rs;
    }

    public int getTotalByTableId(int id){
        int rs = 0;
        SQLiteDatabase database = databaseUtils.getWritableDatabase();
        String sql = "SELECT \n" +
                "    mi.menu_item_price, \n" +
                "    tmi.quantity \n" +
                "FROM \n" +
                "    menu_items mi\n" +
                "JOIN \n" +
                "    table_menu_items tmi\n" +
                "ON \n" +
                "    mi.menu_item_id = tmi.menu_item_id\n" +
                "WHERE \n" +
                "    tmi.table_id = ?;";

        Cursor cursor = database.rawQuery(sql, new String[]{id+""});
        if(cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                int price = cursor.getInt(cursor.getColumnIndexOrThrow("menu_item_price"));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
                rs += price*quantity;
                cursor.moveToNext();
            }
        }
        cursor.close();
        database.close();
        return rs;
    }

    public ArrayList<TableMenuItems> getTableMenuItemsByTableId(int id){
        SQLiteDatabase database = databaseUtils.getWritableDatabase();

        ArrayList<TableMenuItems> tableMenuItems = new ArrayList<>();

        String sql = "SELECT * FROM table_menu_items WHERE table_id=?";
        Cursor cursor = database.rawQuery(sql, new String[]{id+""});
        if(cursor.moveToFirst()){
            while(!cursor.isAfterLast()){
                int table_id = cursor.getInt(cursor.getColumnIndexOrThrow("table_id"));
                int menu_item_id = cursor.getInt(cursor.getColumnIndexOrThrow("menu_item_id"));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
                String order_date = cursor.getString(cursor.getColumnIndexOrThrow("order_date"));
                TableMenuItems table = new TableMenuItems(0, table_id, menu_item_id, quantity, order_date);
                tableMenuItems.add(table);
                cursor.moveToNext();
            }
        }
        cursor.close();
        database.close();
        return tableMenuItems;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean insertTableMenuItems(Map<MenuItems, Integer> list, int tableID){
        SQLiteDatabase database = databaseUtils.getWritableDatabase();
        boolean isSuccess = false;
        try{
            // Bắt đầu 1 giao dịch
            database.beginTransaction();

            // Xóa hết các bản ghi theo table_id
            int rowsDeleted = database.delete("table_menu_items", "table_id=?", new String[]{String.valueOf(tableID)});
            if (rowsDeleted < 0) {
                throw new Exception("Lỗi khi xóa dữ liệu cũ.");
            }

            for( Map.Entry<MenuItems, Integer> entry : list.entrySet() ){
                LocalDateTime currentDateTime = LocalDateTime.now();
                ContentValues values = new ContentValues();
                values.put("table_id", tableID);
                values.put("menu_item_id", entry.getKey().getMenu_item_id());
                values.put("quantity", entry.getValue());
                values.put("order_date", currentDateTime.toString());
                long result = database.insert("table_menu_items", null, values);
                if (result == -1) {
                    throw new Exception("Lỗi khi chèn vào bảng table_menu_items");
                }
            }
            database.setTransactionSuccessful();
            isSuccess = true;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            database.endTransaction();
            database.close();
        }
        return isSuccess;
    }

    public void close() {
        databaseUtils.close();
    }
}
