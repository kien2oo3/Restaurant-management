package com.example.quanlynhahang.entity;

import androidx.annotation.NonNull;

public class TableMenuItems {
    private int table_menu_item_id;
    private int table_id;
    private int menu_item_id;
    private int quantity;
    private String order_date;

    public TableMenuItems() {
    }

    public TableMenuItems(int table_menu_item_id, int table_id, int menu_item_id, int quantity, String order_date) {
        this.table_menu_item_id = table_menu_item_id;
        this.table_id = table_id;
        this.menu_item_id = menu_item_id;
        this.quantity = quantity;
        this.order_date = order_date;
    }

    public int getTable_menu_item_id() {
        return table_menu_item_id;
    }

    public void setTable_menu_item_id(int table_menu_item_id) {
        this.table_menu_item_id = table_menu_item_id;
    }

    public int getTable_id() {
        return table_id;
    }

    public void setTable_id(int table_id) {
        this.table_id = table_id;
    }

    public int getMenu_item_id() {
        return menu_item_id;
    }

    public void setMenu_item_id(int menu_item_id) {
        this.menu_item_id = menu_item_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    @NonNull
    @Override
    public String toString() {
        return table_menu_item_id +
                " - " + table_id +
                " - " + menu_item_id +
                " - " + quantity +
                " - " + order_date;
    }
}
