package com.example.quanlynhahang.entity;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Employees implements Serializable {
    private int employee_id;
    private String full_name;
    private String phone_number;
    private String position;
    private int salary;

    public Employees() {
    }

    public Employees(int employee_id, String full_name, String phone_number, String position, int salary) {
        this.employee_id = employee_id;
        this.full_name = full_name;
        this.phone_number = phone_number;
        this.position = position;
        this.salary = salary;
    }

    public int getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    @NonNull
    @Override
    public String toString() {
        return employee_id +
                " - " + full_name +
                " - " + phone_number +
                " - " + position +
                " - " + salary;
    }
}
