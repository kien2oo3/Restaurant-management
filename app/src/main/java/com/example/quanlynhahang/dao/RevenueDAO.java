package com.example.quanlynhahang.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.quanlynhahang.QuanLyBanAnActivity;
import com.example.quanlynhahang.entity.Revenue;
import com.example.quanlynhahang.entity.Tables;
import com.example.quanlynhahang.features.EditTableActivity;
import com.example.quanlynhahang.utils.DatabaseUtils;

public class RevenueDAO {
    private DatabaseUtils databaseUtils;

    public RevenueDAO(Context context) {
        databaseUtils = new DatabaseUtils(context);
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
