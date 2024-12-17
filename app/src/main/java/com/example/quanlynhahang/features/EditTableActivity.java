package com.example.quanlynhahang.features;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.example.quanlynhahang.Adapters.MenuItemForTableAdapter;
import com.example.quanlynhahang.QuanLyBanAnActivity;
import com.example.quanlynhahang.QuanLyMonAnActivity;
import com.example.quanlynhahang.R;
import com.example.quanlynhahang.dao.TableDAO;
import com.example.quanlynhahang.dao.TableMenuItemDAO;
import com.example.quanlynhahang.entity.Tables;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class EditTableActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edtNewTableNumber, edtNewTableCapacity, edtNewTabledate, edtTableTotal;
    Spinner spinerNewTableStatus;
    Button btnToUpdateMon, btnUpdateTable, btnDeleteTable, btnPayMoney;
    Toolbar toolbarEditBanAn;

    private int tableId;
    private String tableDate;

    TableDAO tableDAO = null;
    TableMenuItemDAO tableMenuItemDAO = null;

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

        getWidgets();

        // Them lua chon spiner:
        listSpiner = new ArrayList<>();
        listSpiner.add("Trống");
        listSpiner.add("Đang sử dụng");
        adapter = new ArrayAdapter<>(EditTableActivity.this, android.R.layout.simple_spinner_dropdown_item, listSpiner);
        spinerNewTableStatus.setAdapter(adapter);

        Intent myItent = getIntent();
        if(myItent!=null){
            int table_id = myItent.getIntExtra("table_id", -1);
            if(table_id!=-1){
                Tables table = tableDAO.getTableByID(table_id);
                if(table!=null){
                    edtNewTableNumber.setText(table.getTable_number()+"");
                    edtNewTableCapacity.setText(table.getCapacity()+"");
                    edtNewTabledate.setText(table.getTable_date());
                    tableId = table_id;
                    tableDate = table.getTable_date();
                    // Định dạng giá tiền theo chuẩn Việt Nam
                    DecimalFormat decimalFormat = new DecimalFormat("#,###");
                    String formattedPrice = decimalFormat.format(tableMenuItemDAO.getTotalByTableId(table_id)) + "₫";
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
        if(actionBar!=null){
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
                    if(resultCode == Activity.RESULT_OK && data != null){
                        int total = data.getIntExtra("total", 0);
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
        if(btnToUpdateMon == view){
            Intent myItent = new Intent(EditTableActivity.this, UpdateMenuItemForTableActivity.class);
            myItent.putExtra("table_id", tableId);
            myItent.putExtra("table_date", tableDate);
            activityResultLauncher.launch(myItent);
        }else if(btnUpdateTable == view){
            if(checkEditBox()){
                if(tableDAO.editTableById(getTable())>0){
                    Toast.makeText(this, "Đã cập nhật lại thông tin cho bàn!", Toast.LENGTH_SHORT).show();
                    Intent myItent = new Intent(EditTableActivity.this, QuanLyBanAnActivity.class);
                    startActivity(myItent);
                }else{
                    Toast.makeText(this, "Lỗi khi cập nhật lại thông tin cho bàn!", Toast.LENGTH_SHORT).show();
                }
            }
        }else if(btnDeleteTable == view){
            if(checkEditBox()){
                showAlertDialog(tableId);
            }
        }else if(btnPayMoney == view){
            
        }
    }
    
    private void showAlertDialog(int tableId){
        AlertDialog.Builder builder = new AlertDialog.Builder(EditTableActivity.this);
        builder.setTitle("Xóa bàn ăn");
        builder.setMessage("Bạn chắc chắn muốn xóa?");
        builder.setIcon(R.drawable.icon_delete);
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(tableDAO.deleteTableById(tableId)){
                    Toast.makeText(EditTableActivity.this, "Xóa bàn ăn thành công!", Toast.LENGTH_SHORT).show();
                    Intent myItent = new Intent(EditTableActivity.this, QuanLyBanAnActivity.class);
                    startActivity(myItent);
                }else{
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

    private Tables getTable(){
        Tables table = new Tables();
        table.setTable_id(tableId);
        table.setTable_date(edtNewTabledate.getText().toString());
        table.setTable_number(Integer.parseInt(edtNewTableNumber.getText().toString()));
        table.setStatus(spinerNewTableStatus.getSelectedItem().toString());
        table.setCapacity(Integer.parseInt(edtNewTableCapacity.getText().toString()));
        return table;
    }

    private boolean checkEditBox(){
        if(edtNewTableNumber.getText().toString().isEmpty()){
            edtNewTableNumber.requestFocus();
            edtNewTableNumber.setError("Không để trống tên bàn!");
            return false;
        }
        if(edtNewTableCapacity.getText().toString().isEmpty()){
            edtNewTableCapacity.requestFocus();
            edtNewTableCapacity.setError("Không để trống số chỗ ngồi!");
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