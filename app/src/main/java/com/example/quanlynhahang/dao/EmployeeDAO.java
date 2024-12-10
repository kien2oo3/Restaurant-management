package com.example.quanlynhahang.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanlynhahang.entity.Employees;
import com.example.quanlynhahang.utils.DatabaseUtils;

import java.util.ArrayList;

public class EmployeeDAO {
    private DatabaseUtils databaseUtils;

    public EmployeeDAO(Context context) {
        databaseUtils = new DatabaseUtils(context);
    }

    public ArrayList<Employees> getAllEmployee() {
        ArrayList<Employees> employees = new ArrayList<>();
        SQLiteDatabase database = databaseUtils.getReadableDatabase();
        String sql = "SELECT * FROM employees";
        Cursor cursor = database.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                int employee_id = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("employee_id")));
                String full_name = cursor.getString(cursor.getColumnIndexOrThrow("full_name"));
                String phone_number = cursor.getString(cursor.getColumnIndexOrThrow("phone_number"));
                String position = cursor.getString(cursor.getColumnIndexOrThrow("position"));
                int salary = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("salary")));
                Employees e = new Employees(employee_id, full_name, phone_number, position, salary);
                employees.add(e);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return employees;
    }

    public long addNewEmployee(Employees e) {
        SQLiteDatabase database = databaseUtils.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("full_name", e.getFull_name());
        values.put("phone_number", e.getPhone_number());
        values.put("position", e.getPosition());
        values.put("salary", e.getSalary());
        return database.insert("employees", null, values);
    }

    public int editEmployeeByID(Employees e) {
        SQLiteDatabase database = databaseUtils.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("full_name", e.getFull_name());
        values.put("phone_number", e.getPhone_number());
        values.put("position", e.getPosition());
        values.put("salary", e.getSalary());
        String whereClause = "employee_id=?";
        String[] whereArgs = new String[]{e.getEmployee_id() + ""};
        return database.update("employees", values, whereClause, whereArgs);
    }

    public int deleteEmployeeByID(int id) {
        SQLiteDatabase database = databaseUtils.getWritableDatabase();
        String whereClause = "employee_id=?";
        String[] whereArgs = new String[]{id + ""};
        return database.delete("employees", whereClause, whereArgs);
    }

    public void close() {
        databaseUtils.close();
    }
}
