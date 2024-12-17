package com.example.quanlynhahang.dao;

import android.content.Context;

import com.example.quanlynhahang.entity.Revenue;
import com.example.quanlynhahang.utils.DatabaseUtils;

public class RevenueDAO {
    private DatabaseUtils databaseUtils;

    public RevenueDAO(Context context) {
        databaseUtils = new DatabaseUtils(context);
    }

//    public long addNewRevenue(Revenue){
//
//    }
}
