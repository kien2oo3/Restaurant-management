package com.example.quanlynhahang.entity;

public class RevenueDetail {
    private int detail_table_name;
    private String detail_menu_date;
    private String detail_item_name;
    private int detail_item_price;
    private int detail_item_quantity;
    private String detail_item_order_date;

    public RevenueDetail() {
    }

    public RevenueDetail(int detail_table_name, String detail_menu_date, String detail_item_name, int detail_item_price, int detail_item_quantity, String detail_item_order_date) {
        this.detail_table_name = detail_table_name;
        this.detail_menu_date = detail_menu_date;
        this.detail_item_name = detail_item_name;
        this.detail_item_price = detail_item_price;
        this.detail_item_quantity = detail_item_quantity;
        this.detail_item_order_date = detail_item_order_date;
    }

    public int getDetail_table_name() {
        return detail_table_name;
    }

    public void setDetail_table_name(int detail_table_name) {
        this.detail_table_name = detail_table_name;
    }

    public String getDetail_menu_date() {
        return detail_menu_date;
    }

    public void setDetail_menu_date(String detail_menu_date) {
        this.detail_menu_date = detail_menu_date;
    }

    public String getDetail_item_name() {
        return detail_item_name;
    }

    public void setDetail_item_name(String detail_item_name) {
        this.detail_item_name = detail_item_name;
    }

    public int getDetail_item_price() {
        return detail_item_price;
    }

    public void setDetail_item_price(int detail_item_price) {
        this.detail_item_price = detail_item_price;
    }

    public int getDetail_item_quantity() {
        return detail_item_quantity;
    }

    public void setDetail_item_quantity(int detail_item_quantity) {
        this.detail_item_quantity = detail_item_quantity;
    }

    public String getDetail_item_order_date() {
        return detail_item_order_date;
    }

    public void setDetail_item_order_date(String detail_item_order_date) {
        this.detail_item_order_date = detail_item_order_date;
    }
}
