package com.example.quanlynhahang.features;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quanlynhahang.R;
import com.example.quanlynhahang.dao.TableDAO;
import com.example.quanlynhahang.entity.Tables;

import java.util.ArrayList;
import java.util.Calendar;

public class AddTableActivity extends AppCompatActivity {
    EditText edtTableNumber, edtTableCapacity, edtTabledate;
    Spinner spinerTableStatus;
    Button btnAddTable;
    Toolbar toolbarAddBanAn;

    TableDAO tableDAO = null;

    ArrayList<String> listSpiner = null;
    ArrayAdapter<String> adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_table);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tableDAO = new TableDAO(AddTableActivity.this);

        getWidgets();

        edtTabledate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        btnAddTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkEditBox()){
                    if(tableDAO.addNewTable(getTable())!=-1){
                        Toast.makeText(AddTableActivity.this, "Thêm bàn ăn thành công!", Toast.LENGTH_SHORT).show();
                        clearEditBox();
                    }else{
                        Toast.makeText(AddTableActivity.this, "Lỗi khi them bàn ăn!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void getWidgets() {
        edtTableNumber = findViewById(R.id.edtTableNumber);
        edtTableCapacity = findViewById(R.id.edtTableCapacity);
        edtTabledate = findViewById(R.id.edtTabledate);
        spinerTableStatus = findViewById(R.id.spinerTableStatus);
        btnAddTable = findViewById(R.id.btnAddTable);

        // Them lua chon spiner:
        listSpiner = new ArrayList<>();
        listSpiner.add("Trống");
        listSpiner.add("Đang sử dụng");
        adapter = new ArrayAdapter<>(AddTableActivity.this, android.R.layout.simple_spinner_dropdown_item, listSpiner);
        spinerTableStatus.setAdapter(adapter);

        toolbarAddBanAn = findViewById(R.id.toolbarAddBanAn);
        setSupportActionBar(toolbarAddBanAn);

        // them nut back tren menu
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void showDatePicker() {
        // Khởi tạo đối tượng Calendar
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Tạo và hiển thị DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year1, month1, dayOfMonth) -> {
                    // Định dạng ngày theo kiểu "dd-MM-yyyy"
                    String date = String.format("%02d-%02d-%04d", dayOfMonth, month1 + 1, year1);
                    edtTabledate.setText(date);
                },
                year, month, day
        );

        datePickerDialog.show();
    }

    private boolean checkEditBox() {
        if (edtTableNumber.getText().toString().isEmpty()) {
            edtTableNumber.setError("Không để trống tên bàn!");
            edtTableNumber.requestFocus();
            return false;
        }
        if (edtTableCapacity.getText().toString().isEmpty()) {
            edtTableCapacity.setError("Không để trống số chỗ ngồi!");
            edtTableCapacity.requestFocus();
        }
        if (edtTabledate.getText().toString().isEmpty()) {
            edtTabledate.setError("Hãy chọn ngày tạo bàn ăn!");
            edtTabledate.requestFocus();
        }
        return true;
    }

    private Tables getTable(){
        Tables t = new Tables();
        t.setTable_id(0);
        t.setTable_number(Integer.parseInt(edtTableNumber.getText().toString()));
        t.setCapacity(Integer.parseInt(edtTableCapacity.getText().toString()));
        t.setStatus(spinerTableStatus.getSelectedItem().toString());
        t.setTable_date(edtTabledate.getText().toString());
        return t;
    }

    private void clearEditBox(){
        edtTabledate.setText("");
        edtTableCapacity.setText("");
        edtTableNumber.setText("");
        spinerTableStatus.setSelection(0);
    }

    @Override
    protected void onDestroy() {
        tableDAO.close();
        super.onDestroy();
    }
}