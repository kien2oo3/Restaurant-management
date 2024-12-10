package com.example.quanlynhahang.entity;

import androidx.annotation.NonNull;

public class Revenue {
    private String revenue_date;
    private int revenue_amount;

    public Revenue() {
    }

    public Revenue(String revenue_date, int revenue_amount) {
        this.revenue_date = revenue_date;
        this.revenue_amount = revenue_amount;
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

    @NonNull
    @Override
    public String toString() {
        return revenue_date + " - " + revenue_amount;
    }
}
