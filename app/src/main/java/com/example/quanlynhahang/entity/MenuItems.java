package com.example.quanlynhahang.entity;

import androidx.annotation.NonNull;

public class MenuItems {
    private int menu_item_id;
    private String menu_item_name;
    private String menu_item_description;
    private int menu_item_price;
    private String image;

    public MenuItems() {
    }

    public MenuItems(int menu_item_id, String menu_item_name, String menu_item_description, int menu_item_price, String image) {
        this.menu_item_id = menu_item_id;
        this.menu_item_name = menu_item_name;
        this.menu_item_description = menu_item_description;
        this.menu_item_price = menu_item_price;
        this.image = image;
    }

    public int getMenu_item_id() {
        return menu_item_id;
    }

    public void setMenu_item_id(int menu_item_id) {
        this.menu_item_id = menu_item_id;
    }

    public String getMenu_item_name() {
        return menu_item_name;
    }

    public void setMenu_item_name(String menu_item_name) {
        this.menu_item_name = menu_item_name;
    }

    public String getMenu_item_description() {
        return menu_item_description;
    }

    public void setMenu_item_description(String menu_item_description) {
        this.menu_item_description = menu_item_description;
    }

    public int getMenu_item_price() {
        return menu_item_price;
    }

    public void setMenu_item_price(int menu_item_price) {
        this.menu_item_price = menu_item_price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @NonNull
    @Override
    public String toString() {
        return menu_item_id +
                " - " + menu_item_name +
                " - " + menu_item_description +
                " - " + menu_item_price +
                " - " + image;
    }
}
