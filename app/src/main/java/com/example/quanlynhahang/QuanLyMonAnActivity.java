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

import com.example.quanlynhahang.Adapters.MenuItemAdapter;
import com.example.quanlynhahang.dao.MenuItemDAO;
import com.example.quanlynhahang.entity.MenuItems;
import com.example.quanlynhahang.features.AddMenuItemActivity;

import java.util.ArrayList;

public class QuanLyMonAnActivity extends AppCompatActivity {
    MenuItemDAO menuItemDAO = null;

    Button btnToAddMenuItem;
    ListView lvMonAn;
    Toolbar toolbarQLMON;
    ArrayList<MenuItems> myArr = null;
    MenuItemAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quan_ly_mon_an);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        menuItemDAO = new MenuItemDAO(QuanLyMonAnActivity.this);

        getWidgets();

        myArr = menuItemDAO.getAllMenuItem();
        adapter = new MenuItemAdapter(QuanLyMonAnActivity.this, R.layout.layout_item_monan, myArr);
        lvMonAn.setAdapter(adapter);

        btnToAddMenuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(QuanLyMonAnActivity.this, AddMenuItemActivity.class);
                startActivity(myIntent);
            }
        });
    }

    private void getWidgets() {
        btnToAddMenuItem = findViewById(R.id.btnToAddMenuItem);
        lvMonAn = findViewById(R.id.lvMonAn);

        // Hien thi nut back tren menu
        toolbarQLMON = findViewById(R.id.toolbarQLMON);
        setSupportActionBar(toolbarQLMON);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onDestroy() {
        menuItemDAO.close();
        super.onDestroy();
    }
}