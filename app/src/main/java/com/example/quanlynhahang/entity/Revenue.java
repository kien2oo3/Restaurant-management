package com.example.quanlynhahang.entity;

import androidx.annotation.NonNull;

public class Revenue {
    private String revenue_date;
    private int revenue_amount;
    private int table_id;

    public Revenue() {
    }

    public Revenue(String revenue_date, int revenue_amount, int table_id) {
        this.revenue_date = revenue_date;
        this.revenue_amount = revenue_amount;
        this.table_id = table_id;
    }

    public String getRevenue_date() {
        return revenue_date;
    }

    public void setRevenue_date(String revenue_date) {
        this.revenue_date = revenue_date;
    }

    public int getRevenue_amount() {
        return revenue_amount;
    }

    public void setRevenue_amount(int revenue_amount) {
        this.revenue_amount = revenue_amount;
    }

    public int getTable_id() {
        return table_id;
    }

    public void setTable_id(int table_id) {
        this.table_id = table_id;
    }

    @NonNull
    @Override
    public String toString() {
        return revenue_date + " - " + revenue_amount + " - " + table_id;
    }
}
