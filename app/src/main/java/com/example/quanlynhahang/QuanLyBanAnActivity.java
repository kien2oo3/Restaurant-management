package com.example.quanlynhahang;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quanlynhahang.Adapters.TableAdapter;
import com.example.quanlynhahang.dao.TableDAO;
import com.example.quanlynhahang.entity.Tables;
import com.example.quanlynhahang.features.AddTableActivity;
import com.example.quanlynhahang.features.EditTableActivity;

import java.util.ArrayList;

public class QuanLyBanAnActivity extends AppCompatActivity {
    ListView lvBanAn;
    Button btnToAddTable;
    Toolbar toolbarQLBanAn;

    TableDAO tableDAO = null;

    ArrayList<Tables> myArr = null;
    TableAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quan_ly_ban_an);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tableDAO = new TableDAO(QuanLyBanAnActivity.this);

        getWidgets();

        myArr = tableDAO.getAllTables();
        adapter = new TableAdapter(QuanLyBanAnActivity.this, R.layout.layout_item_banan, myArr);
        lvBanAn.setAdapter(adapter);

        lvBanAn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent myItent = new Intent(QuanLyBanAnActivity.this, EditTableActivity.class);
                Tables table = myArr.get(i);
                myItent.putExtra("table_id", table.getTable_id());
                startActivity(myItent);
            }
        });

        btnToAddTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(QuanLyBanAnActivity.this, AddTableActivity.class);
                startActivity(myIntent);
            }
        });
    }

    private void getWidgets() {
        lvBanAn = findViewById(R.id.lvBanAn);
        btnToAddTable = findViewById(R.id.btnToAddTable);

        toolbarQLBanAn = findViewById(R.id.toolbarQLBanAn);
        setSupportActionBar(toolbarQLBanAn);
        // Nut back tren thanh menu
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onDestroy() {
        tableDAO.close();
        super.onDestroy();
    }
}