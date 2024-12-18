package com.example.quanlynhahang.features;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quanlynhahang.Adapters.RevenueAdapter;
import com.example.quanlynhahang.Adapters.RevenueDetailAdapter;
import com.example.quanlynhahang.R;
import com.example.quanlynhahang.dao.RevenueDAO;
import com.example.quanlynhahang.entity.RevenueDetail;

import java.util.ArrayList;

public class RevenueDetailActivity extends AppCompatActivity {
    TextView txtDetailTableName, txtDetailMenuDate;
    ListView lvDetailMenuItem;
    Toolbar toolbarRevenueDetail;

    RevenueDAO revenueDAO = null;

    ArrayList<RevenueDetail> myArr = null;
    RevenueDetailAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_revenue_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        revenueDAO = new RevenueDAO(RevenueDetailActivity.this);

        getWidgets();

        Intent myIntent = getIntent();
        if (myIntent != null) {
            int tableId = myIntent.getIntExtra("tableId", -1);
            if (tableId != -1) {
                myArr = revenueDAO.getAllRevenueDetailByTableID(tableId);
                adapter = new RevenueDetailAdapter(RevenueDetailActivity.this, R.layout.layout_item_chi_tiet_theo_ban, myArr);
                lvDetailMenuItem.setAdapter(adapter);
                txtDetailTableName.setText("Bàn số " + myArr.get(0).getDetail_table_name());
                txtDetailMenuDate.setText(myArr.get(0).getDetail_menu_date());
            }
        }

    }

    private void getWidgets() {
        txtDetailTableName = findViewById(R.id.txtDetailTableName);
        txtDetailMenuDate = findViewById(R.id.txtDetailMenuDate);
        lvDetailMenuItem = findViewById(R.id.lvDetailMenuItem);

        toolbarRevenueDetail = findViewById(R.id.toolbarRevenueDetail);
        setSupportActionBar(toolbarRevenueDetail);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}