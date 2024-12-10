package com.example.quanlynhahang.entity;

import androidx.annotation.NonNull;

public class DailyMenu {
    private int daily_menu_id;
    private String menu_date;

    public DailyMenu() {
    }

    public DailyMenu(int daily_menu_id, String menu_date) {
        this.daily_menu_id = daily_menu_id;
        this.menu_date = menu_date;
    }

    public int getDaily_menu_id() {
        return daily_menu_id;
    }

    public void setDaily_menu_id(int daily_menu_id) {
        this.daily_menu_id = daily_menu_id;
    }

    public String getMenu_date() {
        return menu_date;
    }

    public void setMenu_date(String menu_date) {
        this.menu_date = menu_date;
    }

    @NonNull
    @Override
    public String toString() {
        return daily_menu_id + " - " + menu_date;
    }
}
