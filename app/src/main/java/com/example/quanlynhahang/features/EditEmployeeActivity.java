package com.example.quanlynhahang.features;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quanlynhahang.QuanLyNhanVienActivity;
import com.example.quanlynhahang.R;
import com.example.quanlynhahang.dao.EmployeeDAO;
import com.example.quanlynhahang.entity.Employees;

public class EditEmployeeActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edtNewFullName, edtNewPosition, edtNewPhoneNumber, edtNewSalary, edtIDEmployee;
    Button btnEditEmployee, btnDeleteEmployee;
    Toolbar toolbarAddEmployee;

    EmployeeDAO employeeDAO = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_employee);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        employeeDAO = new EmployeeDAO(EditEmployeeActivity.this);

        getWidgets();

        getDataFromItent();

        // Gan su kien click
        btnEditEmployee.setOnClickListener(this);
        btnDeleteEmployee.setOnClickListener(this);
    }

    private void getWidgets() {
        edtIDEmployee = findViewById(R.id.edtIDEmployee);
        edtNewFullName = findViewById(R.id.edtNewFullName);
        edtNewPosition = findViewById(R.id.edtNewPosition);
        edtNewPhoneNumber = findViewById(R.id.edtNewPhoneNumber);
        edtNewSalary = findViewById(R.id.edtNewSalary);
        btnEditEmployee = findViewById(R.id.btnEditEmployee);
        btnDeleteEmployee = findViewById(R.id.btnDeleteEmployee);

        // hhien thi nut back tren menu
        toolbarAddEmployee = findViewById(R.id.toolbarAddEmployee);
        setSupportActionBar(toolbarAddEmployee);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void getDataFromItent() {
        Intent myIntent = getIntent();
        if (myIntent != null) {
            Bundle myBundle = myIntent.getBundleExtra("mybundle");
            if (myBundle != null) {
                Employees e = (Employees) myBundle.getSerializable("employee");
                if (e != null) {
                    edtIDEmployee.setText(e.getEmployee_id() + "");
                    edtNewFullName.setText(e.getFull_name());
                    edtNewPosition.setText(e.getPosition());
                    edtNewPhoneNumber.setText(e.getPhone_number());
                    edtNewSalary.setText(e.getSalary() + "");
                }
            }
        }

    }

    private boolean checkEditText() {
        if (edtNewFullName.getText().toString().isEmpty()) {
            edtNewFullName.setError("Không để trống họ tên!");
            edtNewFullName.requestFocus();
            return false;
        }
        if (edtNewPosition.getText().toString().isEmpty()) {
            edtNewPosition.setError("Không để trống vị trí!");
            edtNewPosition.requestFocus();
            return false;
        }
        if (edtNewPhoneNumber.getText().toString().isEmpty()) {
            edtNewPhoneNumber.setError("Không để trống số điện thoại!");
            edtNewPhoneNumber.requestFocus();
            return false;
        }
        if (edtNewSalary.getText().toString().isEmpty()) {
            edtNewSalary.setError("Không để trống tiền lương!");
            edtNewSalary.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        int id = Integer.parseInt(edtIDEmployee.getText().toString());
        if (btnEditEmployee == view) {
            if (checkEditText()) {
                String newfullname = edtNewFullName.getText().toString();
                String newphonenumber = edtNewPhoneNumber.getText().toString();
                String newposition = edtNewPosition.getText().toString();
                int newsalary = Integer.parseInt(edtNewSalary.getText().toString());
                Employees e = new Employees(id, newfullname, newphonenumber, newposition, newsalary);
                if (employeeDAO.editEmployeeByID(e) > 0) {
                    Toast.makeText(this, "Sửa nhân viên thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Lỗi khi sửa nhân viên!", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (btnDeleteEmployee == view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(EditEmployeeActivity.this);
            builder.setTitle("Xóa nhân viên " + edtNewFullName.getText().toString());
            builder.setMessage("Bạn chắc chắn muốn xóa?");
            builder.setIcon(R.drawable.icon_delete);
            builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (employeeDAO.deleteEmployeeByID(id) > 0) {
                        Toast.makeText(EditEmployeeActivity.this, "Xóa nhân viên thành công!", Toast.LENGTH_SHORT).show();
                        Intent myIntent = new Intent(EditEmployeeActivity.this, QuanLyNhanVienActivity.class);
                        startActivity(myIntent);
                    } else {
                        Toast.makeText(EditEmployeeActivity.this, "Lỗi khi xóa nhân viên!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            builder.create().show();
        }
    }

    @Override
    protected void onDestroy() {
        employeeDAO.close();
        super.onDestroy();
    }
}