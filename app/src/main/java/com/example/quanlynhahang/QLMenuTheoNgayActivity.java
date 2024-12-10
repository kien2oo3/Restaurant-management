package com.example.quanlynhahang;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quanlynhahang.Adapters.DailyMenuAdapter;
import com.example.quanlynhahang.dao.DailyMenuDAO;
import com.example.quanlynhahang.entity.DailyMenu;
import com.example.quanlynhahang.features.AddDailyMenuActivity;
import com.example.quanlynhahang.features.AddMenuItemActivity;

import java.util.ArrayList;

public class QLMenuTheoNgayActivity extends AppCompatActivity implements View.OnClickListener {
    DailyMenuDAO dailyMenuDAO = null;
    Button btnToAddDailyMenuItem;
    ListView lvMonTheoNgay;
    ArrayList<DailyMenu> myArr = null;
    DailyMenuAdapter adapter = null;
    Toolbar toolbarQLMonAnTheoNgay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_qlmenu_theo_ngay);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dailyMenuDAO = new DailyMenuDAO(QLMenuTheoNgayActivity.this);

        getWidgets();

        myArr = dailyMenuDAO.getAllDailyMenu();
        adapter = new DailyMenuAdapter(QLMenuTheoNgayActivity.this, R.layout.layout_item_ngay, myArr);
        lvMonTheoNgay.setAdapter(adapter);

        btnToAddDailyMenuItem.setOnClickListener(this);
    }

    private void getWidgets() {
        btnToAddDailyMenuItem = findViewById(R.id.btnToAddDailyMenuItem);
        lvMonTheoNgay = findViewById(R.id.lvMonTheoNgay);

        toolbarQLMonAnTheoNgay = findViewById(R.id.toolbarQLMonAnTheoNgay);
        setSupportActionBar(toolbarQLMonAnTheoNgay);
        // Hien thi nut back tren menu
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onClick(View view) {
        if (btnToAddDailyMenuItem == view) {
            Intent myItent = new Intent(QLMenuTheoNgayActivity.this, AddDailyMenuActivity.class);
            startActivity(myItent);
        }
    }
}