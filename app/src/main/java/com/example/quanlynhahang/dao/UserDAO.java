package com.example.quanlynhahang.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanlynhahang.entity.User;
import com.example.quanlynhahang.utils.DatabaseUtils;

import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private DatabaseUtils databaseUtils;

    public UserDAO(Context context) {
        databaseUtils = new DatabaseUtils(context);
    }

    private List<User> getAllUser() {
        List<User> users = new ArrayList<>();
        SQLiteDatabase database = databaseUtils.getReadableDatabase();
        String sql = "SELECT * FROM user";
        Cursor cursor = database.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
                String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
                User user = new User(username, password);
                users.add(user);
                cursor.moveToNext();
            }
        }
        cursor.close();
        database.close();
        return users;
    }

    public boolean checkUser(User user) {
        List<User> users = getAllUser();
        for (User x : users) {
            if (x.getUsername().equals(user.getUsername()) && x.getPassword().equals(user.getPassword())) {
                return true;
            }
        }
        return false;
    }

    public boolean checkUserForUsername(User user) {
        List<User> users = getAllUser();
        for (User x : users) {
            if (x.getUsername().equals(user.getUsername())) {
                return true;
            }
        }
        return false;
    }

    public long addNewUser(User user) {
        SQLiteDatabase database = databaseUtils.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", user.getUsername());
        values.put("password", user.getPassword());
        long rs = database.insert("user", null, values);
        database.close();
        return rs;
    }

    public void close() {
        databaseUtils.close();
    }
}
