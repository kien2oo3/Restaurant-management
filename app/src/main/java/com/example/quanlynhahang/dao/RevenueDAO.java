package com.example.quanlynhahang.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.quanlynhahang.QuanLyBanAnActivity;
import com.example.quanlynhahang.entity.Revenue;
import com.example.quanlynhahang.entity.RevenueDetail;
import com.example.quanlynhahang.entity.Tables;
import com.example.quanlynhahang.features.EditTableActivity;
import com.example.quanlynhahang.utils.DatabaseUtils;

import java.util.ArrayList;

public class RevenueDAO {
    private DatabaseUtils databaseUtils;

    public RevenueDAO(Context context) {
        databaseUtils = new DatabaseUtils(context);
    }

    public ArrayList<Revenue> getAllRevenue() {
        SQLiteDatabase database = databaseUtils.getReadableDatabase();
        ArrayList<Revenue> rs = new ArrayList<>();
        String sql = "SELECT * FROM revenue;";
        Cursor cursor = database.rawQuery(sql, null, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("revenue_id"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("revenue_date"));
                int amount = cursor.getInt(cursor.getColumnIndexOrThrow("revenue_amount"));
                int tableId = cursor.getInt(cursor.getColumnIndexOrThrow("table_id"));
                Revenue revenue = new Revenue(id, date, amount, tableId);
                rs.add(revenue);
                cursor.moveToNext();
            }
        }
        cursor.close();
        database.close();
        return rs;
    }

    public ArrayList<RevenueDetail> getAllRevenueDetailByTableID(int tableId) {
        SQLiteDatabase database = databaseUtils.getReadableDatabase();
        ArrayList<RevenueDetail> revenueDetails = new ArrayList<>();
        //String sql = "SELECT tb.table_number, tb.table_date, mi.menu_item_name, mi.menu_item_price, tbi.quantity, tbi.order_date FROM revenue rv JOIN tables tb ON tb.table_id = rv.table_id JOIN table_menu_items tbi ON tbi.table_id = tb.table_id JOIN menu_items mi ON mi.menu_item_id = tbi.menu_item_id WHERE rv.table_id = ?;";
        String sql =
            "SELECT tb.table_number, tb.table_date, mi.menu_item_name, mi.menu_item_price, " +
                    "tbi.quantity, tbi.order_date " +
                    "FROM revenue rv " +
                    "JOIN tables tb ON tb.table_id = rv.table_id " +
                    "JOIN table_menu_items tbi ON tbi.table_id = tb.table_id " +
                    "JOIN menu_items mi ON mi.menu_item_id = tbi.menu_item_id " +
                    "WHERE rv.table_id = ?;";
        Cursor cursor = database.rawQuery(sql, new String[]{tableId+""});
        if(cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                int table_number = cursor.getInt(cursor.getColumnIndexOrThrow("table_number"));
                String table_date = cursor.getString(cursor.getColumnIndexOrThrow("table_date"));
                String menu_item_name = cursor.getString(cursor.getColumnIndexOrThrow("menu_item_name"));
                int menu_item_price = cursor.getInt(cursor.getColumnIndexOrThrow("menu_item_price"));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
                String order_date = cursor.getString(cursor.getColumnIndexOrThrow("order_date"));
                RevenueDetail revenueDetail = new RevenueDetail(table_number, table_date, menu_item_name, menu_item_price, quantity, order_date);
                revenueDetails.add(revenueDetail);
                cursor.moveToNext();
            }
        }
        cursor.close();
        database.close();
        return revenueDetails;
    }

    public long addNewRevenue(Revenue revenue) {
        SQLiteDatabase database = databaseUtils.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("revenue_date", revenue.getRevenue_date());
        values.put("revenue_amount", revenue.getRevenue_amount());
        values.put("table_id", revenue.getTable_id());
        long rs = database.insert("revenue", null, values);
        database.close();
        return rs;
    }

    public boolean paySuccess(Revenue revenue) {
        SQLiteDatabase database = databaseUtils.getWritableDatabase();
        boolean isSuccess = false;
        int tableId = revenue.getTable_id();
        try {
            database.beginTransaction();
            //Thêm hóa đơn
            ContentValues values = new ContentValues();
            values.put("revenue_date", revenue.getRevenue_date());
            values.put("revenue_amount", revenue.getRevenue_amount());
            values.put("table_id", tableId);
            long rs = database.insert("revenue", null, values);
            if (rs == -1) {
                throw new Exception("Lỗi khi thêm vào bảng revenue!");
            }
            // Tìm bàn theo id
            Tables table = null;
            String sql = "SELECT * FROM tables WHERE table_id=?";
            Cursor cursor = database.rawQuery(sql, new String[]{tableId + ""});
            if (cursor.moveToFirst()) {
                int number = cursor.getInt(cursor.getColumnIndexOrThrow("table_number"));
                int capacity = cursor.getInt(cursor.getColumnIndexOrThrow("capacity"));
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("table_date"));
                String isPay = cursor.getString(cursor.getColumnIndexOrThrow("isPay"));
                table = new Tables(tableId, number, capacity, status, date, isPay);
            }
            cursor.close();
            if (table == null) {
                throw new Exception("Không tìm thấy bàn theo table_id = " + tableId);
            }
            // Cập nhật bàn vừa tìm
            table.setIsPay("true"); // Đã thanh toán
            values = new ContentValues();
            values.put("table_number", table.getTable_number());
            values.put("capacity", table.getCapacity());
            values.put("status", table.getStatus());
            values.put("table_date", table.getTable_date());
            values.put("isPay", table.getIsPay());
            String whereClause = "table_id=?";
            String[] whereArgs = new String[]{table.getTable_id() + ""};
            rs = database.update("tables", values, whereClause, whereArgs);
            if (rs <= 0) {
                throw new Exception("Không cập nhật được bàn!");
            }
            // Tạo 1 bàn mới với thông tin giống bàn vừa thanh toán
            Tables newTable = new Tables();
            newTable.setTable_number(table.getTable_number());
            newTable.setCapacity(table.getCapacity());
            newTable.setStatus("Trống");
            newTable.setTable_date(table.getTable_date());
            // Thêm bàn mới vào bảng tables
            values = new ContentValues();
            values.put("table_number", newTable.getTable_number());
            values.put("capacity", newTable.getCapacity());
            values.put("status", newTable.getStatus());
            values.put("table_date", newTable.getTable_date());
            rs = database.insert("tables", null, values);
            if (rs == -1) {
                throw new Exception("Lỗi khi thêm bàn mới vào bảng tables!");
            }
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
