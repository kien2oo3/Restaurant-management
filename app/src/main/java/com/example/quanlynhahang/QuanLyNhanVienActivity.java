package com.example.quanlynhahang;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quanlynhahang.Adapters.EmployeeAdapter;
import com.example.quanlynhahang.dao.EmployeeDAO;
import com.example.quanlynhahang.entity.Employees;
import com.example.quanlynhahang.features.AddEmployeeActivity;
import com.example.quanlynhahang.features.EditEmployeeActivity;

import java.util.ArrayList;

public class QuanLyNhanVienActivity extends AppCompatActivity {
    EmployeeDAO employeeDAO;
    Button btnToAddEmployee;
    ListView lvNhanVien;
    Toolbar toolbarQLNV;
    ArrayList<Employees> myArr = null;
    EmployeeAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quan_ly_nhan_vien);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        employeeDAO = new EmployeeDAO(QuanLyNhanVienActivity.this);

        getWidgets();

        myArr = employeeDAO.getAllEmployee();
        adapter = new EmployeeAdapter(QuanLyNhanVienActivity.this, R.layout.layout_item_nhanvien, myArr);
        lvNhanVien.setAdapter(adapter);

        lvNhanVien.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Employees employee = myArr.get(i);
                Intent myIntent = new Intent(QuanLyNhanVienActivity.this, EditEmployeeActivity.class);
                Bundle myBundle = new Bundle();
                myBundle.putSerializable("employee", employee);
                myIntent.putExtra("mybundle", myBundle);
                startActivity(myIntent);
            }
        });
        btnToAddEmployee.setOnClickListener(new DoSomeThing());
    }

    private void getWidgets() {
        btnToAddEmployee = findViewById(R.id.btnToAddEmployee);
        lvNhanVien = findViewById(R.id.lvNhanVien);
        toolbarQLNV = findViewById(R.id.toolbarQLNV);
        setSupportActionBar(toolbarQLNV);
        // Nut tro ve tren menu
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onDestroy() {
        employeeDAO.close();
        super.onDestroy();
    }

    class DoSomeThing implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (btnToAddEmployee == view) {
                Intent myIntent = new Intent(QuanLyNhanVienActivity.this, AddEmployeeActivity.class);
                startActivity(myIntent);
            }
        }
    }
}