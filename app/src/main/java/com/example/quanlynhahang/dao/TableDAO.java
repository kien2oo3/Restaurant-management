package com.example.quanlynhahang.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanlynhahang.entity.Tables;
import com.example.quanlynhahang.utils.DatabaseUtils;

import java.util.ArrayList;

public class TableDAO {
    private DatabaseUtils databaseUtils;

    public TableDAO(Context context) {
        databaseUtils = new DatabaseUtils(context);
    }

    public SQLiteDatabase getDataBaseModeWrite(){
        return databaseUtils.getWritableDatabase();
    }

    public Tables getTableByID(int id) {
        SQLiteDatabase database = databaseUtils.getReadableDatabase();
        Tables rs = null;
        String sql = "SELECT * FROM tables WHERE table_id=?";
        Cursor cursor = database.rawQuery(sql, new String[]{id + ""});
        if (cursor.moveToFirst()) {
            int number = cursor.getInt(cursor.getColumnIndexOrThrow("table_number"));
            int capacity = cursor.getInt(cursor.getColumnIndexOrThrow("capacity"));
            String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
            String date = cursor.getString(cursor.getColumnIndexOrThrow("table_date"));
            String isPay = cursor.getString(cursor.getColumnIndexOrThrow("isPay"));
            rs = new Tables(id, number, capacity, status, date, isPay);
        }
        cursor.close();
        database.close();
        return rs;
    }

    public ArrayList<Tables> getAllTables() {
        SQLiteDatabase database = databaseUtils.getReadableDatabase();
        ArrayList<Tables> rs = new ArrayList<>();

        String sql = "SELECT * FROM tables ORDER BY table_number ASC";
        Cursor cursor = database.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String isPay = cursor.getString(cursor.getColumnIndexOrThrow("isPay"));
                if(isPay == null){
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("table_id"));
                    int number = cursor.getInt(cursor.getColumnIndexOrThrow("table_number"));
                    int capacity = cursor.getInt(cursor.getColumnIndexOrThrow("capacity"));
                    String date = cursor.getString(cursor.getColumnIndexOrThrow("table_date"));
                    String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                    Tables table = new Tables(id, number, capacity, status, date, isPay);
                    rs.add(table);
                }

                cursor.moveToNext();
            }
        }
        cursor.close();
        database.close();
        return rs;
    }

    public long addNewTable(Tables table) {
        SQLiteDatabase database = databaseUtils.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("table_number", table.getTable_number());
        values.put("capacity", table.getCapacity());
        values.put("status", table.getStatus());
        values.put("table_date", table.getTable_date());
        long rs = database.insert("tables", null, values);
        database.close();
        return rs;
    }

    public int editTableById(Tables table) {
        SQLiteDatabase database = databaseUtils.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("table_number", table.getTable_number());
        values.put("capacity", table.getCapacity());
        values.put("status", table.getStatus());
        values.put("table_date", table.getTable_date());
        values.put("isPay", table.getIsPay());
        String whereClause = "table_id=?";
        String[] whereArgs = new String[]{table.getTable_id() + ""};
        int rs = database.update("tables", values, whereClause, whereArgs);
        database.close();
        return rs;
    }

    public boolean deleteTableById(int id) {
        SQLiteDatabase database = databaseUtils.getWritableDatabase();
        boolean isSuccess = false;
        try {
            database.beginTransaction();
            // Xoa  phan tu trong bang table_menu_items theo table_id truoc
            database.delete("table_menu_items", "table_id=?", new String[]{id + ""});
            // Tiep theo la xoa bang tables theo table_id
            database.delete("tables", "table_id=?", new String[]{id + ""});
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
