package com.example.quanlynhahang.entity;

import androidx.annotation.NonNull;

public class Tables {
    private int table_id;
    private int table_number;
    private int capacity;
    private String status;
    private String table_date;

    public Tables() {
    }

    public Tables(int table_id, int table_number, int capacity, String status, String table_date) {
        this.table_id = table_id;
        this.table_number = table_number;
        this.capacity = capacity;
        this.status = status;
        this.table_date = table_date;
    }

    public int getTable_id() {
        return table_id;
    }

    public void setTable_id(int table_id) {
        this.table_id = table_id;
    }

    public int getTable_number() {
        return table_number;
    }

    public void setTable_number(int table_number) {
        this.table_number = table_number;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTable_date() {
        return table_date;
    }

    public void setTable_date(String table_date) {
        this.table_date = table_date;
    }

    @NonNull
    @Override
    public String toString() {
        return table_id +
                " - " + table_number +
                " - " + capacity +
                " - " + status +
                " - " + table_date;
    }
}
