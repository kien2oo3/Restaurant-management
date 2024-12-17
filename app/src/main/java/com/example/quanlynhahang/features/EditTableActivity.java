package com.example.quanlynhahang.features;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quanlynhahang.QuanLyBanAnActivity;
import com.example.quanlynhahang.R;
import com.example.quanlynhahang.dao.RevenueDAO;
import com.example.quanlynhahang.dao.TableDAO;
import com.example.quanlynhahang.dao.TableMenuItemDAO;
import com.example.quanlynhahang.entity.Revenue;
import com.example.quanlynhahang.entity.Tables;
import com.example.quanlynhahang.utils.DatabaseUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class EditTableActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edtNewTableNumber, edtNewTableCapacity, edtNewTabledate, edtTableTotal;
    Spinner spinerNewTableStatus;
    Button btnToUpdateMon, btnUpdateTable, btnDeleteTable, btnPayMoney;
    Toolbar toolbarEditBanAn;

    private int tableId;
    private String tableDate;
    private int total;

    TableDAO tableDAO = null;
    TableMenuItemDAO tableMenuItemDAO = null;
    RevenueDAO revenueDAO = null;

    ArrayList<String> listSpiner = null;
    ArrayAdapter<String> adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_table);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tableDAO = new TableDAO(EditTableActivity.this);
        tableMenuItemDAO = new TableMenuItemDAO(EditTableActivity.this);
        revenueDAO = new RevenueDAO(EditTableActivity.this);

        getWidgets();

        // Them lua chon spiner:
        listSpiner = new ArrayList<>();
        listSpiner.add("Trống");
        listSpiner.add("Đang sử dụng");
        adapter = new ArrayAdapter<>(EditTableActivity.this, android.R.layout.simple_spinner_dropdown_item, listSpiner);
        spinerNewTableStatus.setAdapter(adapter);

        Intent myItent = getIntent();
        if (myItent != null) {
            int table_id = myItent.getIntExtra("table_id", -1);
            if (table_id != -1) {
                Tables table = tableDAO.getTableByID(table_id);
                if (table != null) {
                    edtNewTableNumber.setText(table.getTable_number() + "");
                    edtNewTableCapacity.setText(table.getCapacity() + "");
                    edtNewTabledate.setText(table.getTable_date());
                    tableId = table_id;
                    tableDate = table.getTable_date();

                    total = tableMenuItemDAO.getTotalByTableId(table_id);
                    // Định dạng giá tiền theo chuẩn Việt Nam
                    DecimalFormat decimalFormat = new DecimalFormat("#,###");
                    String formattedPrice = decimalFormat.format(total) + "₫";
                    edtTableTotal.setText(formattedPrice);

                    int index = listSpiner.indexOf(table.getStatus());
                    spinerNewTableStatus.setSelection(index);
                }
            }
        }

        btnToUpdateMon.setOnClickListener(this);
        btnUpdateTable.setOnClickListener(this);
        btnDeleteTable.setOnClickListener(this);
        btnPayMoney.setOnClickListener(this);
        edtNewTabledate.setOnClickListener(this);
    }

    private void getWidgets() {
        edtNewTableNumber = findViewById(R.id.edtNewTableNumber);
        edtNewTableCapacity = findViewById(R.id.edtNewTableCapacity);
        edtNewTabledate = findViewById(R.id.edtNewTabledate);
        edtTableTotal = findViewById(R.id.edtTableTotal);
        spinerNewTableStatus = findViewById(R.id.spinerNewTableStatus);
        btnToUpdateMon = findViewById(R.id.btnToUpdateMon);
        btnUpdateTable = findViewById(R.id.btnUpdateTable);
        btnDeleteTable = findViewById(R.id.btnDeleteTable);
        btnPayMoney = findViewById(R.id.btnPayMoney);

        toolbarEditBanAn = findViewById(R.id.toolbarEditBanAn);
        setSupportActionBar(toolbarEditBanAn);
        // hien nut back tren menu:
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    int resultCode = result.getResultCode();
                    Intent data = result.getData();
                    if (resultCode == Activity.RESULT_OK && data != null) {
                        total = data.getIntExtra("total", 0);
                        // Định dạng giá tiền theo chuẩn Việt Nam
                        DecimalFormat decimalFormat = new DecimalFormat("#,###");
                        String formattedPrice = decimalFormat.format(total) + "₫";
                        edtTableTotal.setText(formattedPrice);
                    }
                }
            }
    );

    @Override
    public void onClick(View view) {
        if (btnToUpdateMon == view) {
            Intent myItent = new Intent(EditTableActivity.this, UpdateMenuItemForTableActivity.class);
            myItent.putExtra("table_id", tableId);
            myItent.putExtra("table_date", tableDate);
            activityResultLauncher.launch(myItent);
        } else if (btnUpdateTable == view) {
            if (checkEditBox()) {
                if (tableDAO.editTableById(getTable()) > 0) {
                    Toast.makeText(this, "Đã cập nhật lại thông tin cho bàn!", Toast.LENGTH_SHORT).show();
                    Intent myItent = new Intent(EditTableActivity.this, QuanLyBanAnActivity.class);
                    startActivity(myItent);
                } else {
                    Toast.makeText(this, "Lỗi khi cập nhật lại thông tin cho bàn!", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (btnDeleteTable == view) {
            if (checkEditBox()) {
                showAlertDialog(tableId);
            }
        } else if (btnPayMoney == view) {
            if (!spinerNewTableStatus.getSelectedItem().toString().equals("Trống")) {
                spinerNewTableStatus.setBackgroundColor(Color.TRANSPARENT);
                if (total != 0) {
                    // Lấy thời gian hiện tại
                    Calendar calendar = Calendar.getInstance();
                    // Lấy các giá trị ngày, tháng, năm
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH) + 1; // Cộng 1 vì tháng bắt đầu từ 0
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    // Định dạng ngày theo kiểu ngày-tháng-năm
                    String formattedDate = String.format("%02d-%02d-%04d", day, month, year);

                    // Định dạng giá tiền theo chuẩn Việt Nam
                    DecimalFormat decimalFormat = new DecimalFormat("#,###");
                    String formattedTotal = decimalFormat.format(total) + "₫";

                    Revenue revenue = new Revenue();
                    revenue.setRevenue_amount(total);
                    revenue.setTable_id(tableId);
                    revenue.setRevenue_date(formattedDate);
                    showPayDialog(revenue, formattedTotal);
                } else {
                    Toast.makeText(this, "Hóa đơn 0đ không thể thanh toán!", Toast.LENGTH_SHORT).show();
                }
            }else{
                spinerNewTableStatus.setBackgroundColor(Color.RED);
                Toast.makeText(this, "Vui lòng cập nhật lại trạng thái bàn sang đang sử dụng!", Toast.LENGTH_LONG).show();
            }
        } else if (edtNewTabledate == view) {
            showDatePicker();
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
                    edtNewTabledate.setText(date);
                },
                year, month, day
        );

        datePickerDialog.show();
    }

    private void showAlertDialog(int tableId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditTableActivity.this);
        builder.setTitle("Xóa bàn ăn");
        builder.setMessage("Bạn chắc chắn muốn xóa?");
        builder.setIcon(R.drawable.icon_delete);
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (tableDAO.deleteTableById(tableId)) {
                    Toast.makeText(EditTableActivity.this, "Xóa bàn ăn thành công!", Toast.LENGTH_SHORT).show();
                    Intent myItent = new Intent(EditTableActivity.this, QuanLyBanAnActivity.class);
                    startActivity(myItent);
                } else {
                    Toast.makeText(EditTableActivity.this, "Lỗi khi xóa bàn ăn!", Toast.LENGTH_SHORT).show();
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

    private void showPayDialog(Revenue revenue, String formattedTotal) {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditTableActivity.this);
        builder.setTitle("Thanh toán hóa đơn");
        builder.setMessage("Bạn chắc chắn muốn thanh toán hoá đơn " + formattedTotal + "?");
        builder.setIcon(R.drawable.icon_info);
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Tránh cho một phần của quá trình thực thi thành công nhưng phần khác thất bại
                if (revenueDAO.paySuccess(revenue)) {
                    Toast.makeText(EditTableActivity.this, "Thanh toán hóa đơn thành công!", Toast.LENGTH_SHORT).show();
                    // Điều hướng đến giao diện quản lý bàn
                    Intent myItent = new Intent(EditTableActivity.this, QuanLyBanAnActivity.class);
                    startActivity(myItent);
                } else {
                    Toast.makeText(EditTableActivity.this, "Lỗi thanh toán hóa đơn!", Toast.LENGTH_SHORT).show();
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

    private Tables getTable() {
        Tables table = new Tables();
        table.setTable_id(tableId);
        table.setTable_date(edtNewTabledate.getText().toString());
        table.setTable_number(Integer.parseInt(edtNewTableNumber.getText().toString()));
        table.setStatus(spinerNewTableStatus.getSelectedItem().toString());
        table.setCapacity(Integer.parseInt(edtNewTableCapacity.getText().toString()));
        return table;
    }

    private boolean checkEditBox() {
        if (edtNewTableNumber.getText().toString().isEmpty()) {
            edtNewTableNumber.requestFocus();
            edtNewTableNumber.setError("Không để trống tên bàn!");
            return false;
        }
        if (edtNewTableCapacity.getText().toString().isEmpty()) {
            edtNewTableCapacity.requestFocus();
            edtNewTableCapacity.setError("Không để trống số chỗ ngồi!");
            return false;
        }
        if (edtNewTabledate.getText().toString().isEmpty()) {
            edtNewTabledate.requestFocus();
            edtNewTabledate.setError("Không để trống ngày!");
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        tableDAO.close();
        super.onDestroy();
    }
}