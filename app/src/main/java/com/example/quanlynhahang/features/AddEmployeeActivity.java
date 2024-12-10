package com.example.quanlynhahang.features;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quanlynhahang.QuanLyNhanVienActivity;
import com.example.quanlynhahang.R;
import com.example.quanlynhahang.dao.EmployeeDAO;
import com.example.quanlynhahang.entity.Employees;

public class AddEmployeeActivity extends AppCompatActivity {
    Button btnAddEmployee;
    EmployeeDAO employeeDAO = null;
    EditText edtFullName, edtPhoneNumber, edtPosition, edtSalary;
    Toolbar toolbarAddEmployee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_employee);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        employeeDAO = new EmployeeDAO(AddEmployeeActivity.this);

        getWidgets();

        btnAddEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullname = edtFullName.getText().toString();
                if (fullname.isEmpty()) {
                    Toast.makeText(AddEmployeeActivity.this, "Không để trống họ tên!", Toast.LENGTH_SHORT).show();
                    edtFullName.requestFocus();
                    return;
                }
                String phonenumber = edtPhoneNumber.getText().toString();
                if (phonenumber.isEmpty()) {
                    Toast.makeText(AddEmployeeActivity.this, "Không để trống số điện thoại!", Toast.LENGTH_SHORT).show();
                    edtPhoneNumber.requestFocus();
                    return;
                }
                String position = edtPosition.getText().toString();
                if (position.isEmpty()) {
                    Toast.makeText(AddEmployeeActivity.this, "Không để trống vị trí!", Toast.LENGTH_SHORT).show();
                    edtPosition.requestFocus();
                    return;
                }
                String salary = edtSalary.getText().toString();
                if (salary.isEmpty()) {
                    Toast.makeText(AddEmployeeActivity.this, "Không để trống lương!", Toast.LENGTH_SHORT).show();
                    edtSalary.requestFocus();
                    return;
                }
                Employees e = new Employees(0, fullname, phonenumber, position, Integer.parseInt(salary.trim()));
                if (employeeDAO.addNewEmployee(e) != -1) {
                    Toast.makeText(AddEmployeeActivity.this, "Thêm nhân viên thành công!", Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(AddEmployeeActivity.this, QuanLyNhanVienActivity.class);
                    startActivity(myIntent);
                } else {
                    Toast.makeText(AddEmployeeActivity.this, "Lỗi thêm nhân viên!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getWidgets() {
        btnAddEmployee = findViewById(R.id.btnAddEmployee);
        edtFullName = findViewById(R.id.edtFullName);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        edtPosition = findViewById(R.id.edtPosition);
        edtSalary = findViewById(R.id.edtSalary);

        // Hien thi nut back tren menu:
        toolbarAddEmployee = findViewById(R.id.toolbarAddEmployee);
        setSupportActionBar(toolbarAddEmployee);
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
}