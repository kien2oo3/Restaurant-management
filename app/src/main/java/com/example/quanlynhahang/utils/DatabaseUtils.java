package com.example.quanlynhahang.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseUtils extends SQLiteOpenHelper {
    private static String TAG = "DatabaseUtils";

    private static final String DATABASE_NAME = "QLNhaHang.db";
    private static final int DATABASE_VERSION = 1;
    private static String DATABASE_PATH = "";

    private Context context;

    public DatabaseUtils(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        DATABASE_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        Log.i(TAG, "Database Path: " + DATABASE_PATH + DATABASE_NAME);
        initDataBase();
    }

    private void initDataBase() {
        if (!isDataBaseExists()) {
            this.getReadableDatabase();
            this.close();
            try {
                copyDataBase();
                Log.e(TAG, "Database created");
            } catch (IOException exception) {
                throw new Error("Error Copying DataBase");
            }
        }
    }

    private boolean isDataBaseExists() {
        File databaseFile = new File(DATABASE_PATH + DATABASE_NAME);
        return databaseFile.exists();
    }

    private void copyDataBase() throws IOException {
        InputStream inputStream = context.getAssets().open(DATABASE_NAME);
        String databasePath = DATABASE_PATH + DATABASE_NAME;
        OutputStream outputStream = new FileOutputStream(databasePath, false);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Không cần logic trong onCreate vì database được sao chép từ assets
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Logic để nâng cấp cơ sở dữ liệu khi phiên bản thay đổi
    }
}
