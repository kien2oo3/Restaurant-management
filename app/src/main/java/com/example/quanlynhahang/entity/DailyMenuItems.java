package com.example.quanlynhahang.entity;

import androidx.annotation.NonNull;

public class DailyMenuItems {
    private int daily_menu_item_id;
    private int daily_menu_id;
    private int menu_item_id;

    public DailyMenuItems() {
    }

    public DailyMenuItems(int daily_menu_item_id, int daily_menu_id, int menu_item_id) {
        this.daily_menu_item_id = daily_menu_item_id;
        this.daily_menu_id = daily_menu_id;
        this.menu_item_id = menu_item_id;
    }

    public int getDaily_menu_item_id() {
        return daily_menu_item_id;
    }

    public void setDaily_menu_item_id(int daily_menu_item_id) {
        this.daily_menu_item_id = daily_menu_item_id;
    }

    public int getDaily_menu_id() {
        return daily_menu_id;
    }

    public void setDaily_menu_id(int daily_menu_id) {
        this.daily_menu_id = daily_menu_id;
    }

    public int getMenu_item_id() {
        return menu_item_id;
    }

    public void setMenu_item_id(int menu_item_id) {
        this.menu_item_id = menu_item_id;
    }

    @NonNull
    @Override
    public String toString() {
        return daily_menu_item_id +
                " - " + daily_menu_id +
                " - " + menu_item_id;
    }
}
