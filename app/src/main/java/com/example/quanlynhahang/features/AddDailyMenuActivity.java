package com.example.quanlynhahang.features;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quanlynhahang.Adapters.DailyMenuItemAdapter;
import com.example.quanlynhahang.Adapters.MenuItemAdapter;
import com.example.quanlynhahang.R;
import com.example.quanlynhahang.dao.DailyMenuItemDAO;
import com.example.quanlynhahang.dao.MenuItemDAO;
import com.example.quanlynhahang.entity.MenuItems;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;

public class AddDailyMenuActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edtSelectDate;
    Button btnAddDailyMenuItem;
    ListView lvMonAn;
    Toolbar toolbarAddMonAnTheoNgay;

    MenuItemDAO menuItemDAO = null;
    DailyMenuItemDAO dailyMenuItemDAO = null;

    ArrayList<MenuItems> myArr = null;
    DailyMenuItemAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_daily_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        menuItemDAO = new MenuItemDAO(AddDailyMenuActivity.this);
        dailyMenuItemDAO = new DailyMenuItemDAO(AddDailyMenuActivity.this);

        getWidgets();

        myArr = menuItemDAO.getAllMenuItem();
        adapter = new DailyMenuItemAdapter(AddDailyMenuActivity.this, R.layout.layout_item_mon_theo_ngay, myArr);
        lvMonAn.setAdapter(adapter);

        edtSelectDate.setOnClickListener(this);
        btnAddDailyMenuItem.setOnClickListener(this);


    }

    private void getWidgets() {
        edtSelectDate = findViewById(R.id.edtSelectDate);
        btnAddDailyMenuItem = findViewById(R.id.btnAddDailyMenuItem);
        lvMonAn = findViewById(R.id.lvMonAn);

        toolbarAddMonAnTheoNgay = findViewById(R.id.toolbarAddMonAnTheoNgay);
        setSupportActionBar(toolbarAddMonAnTheoNgay);

        // hien thi nut back tren menu
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onClick(View view) {
        if (btnAddDailyMenuItem == view) {
            if(edtSelectDate.getText().toString().isEmpty()){
                edtSelectDate.setError("Phải chọn ngày để thêm!");
                return;
            }
            Set<MenuItems> itemsSelected = adapter.getSelectedItems();
            if(itemsSelected.isEmpty()){
                Toast.makeText(this, "Hãy chọn ít nhất 1 món ăn cho menu!", Toast.LENGTH_SHORT).show();
                return;
            }
            ArrayList<MenuItems> temp = new ArrayList<>(itemsSelected);
            if(dailyMenuItemDAO.insertMenuItemsForDate(edtSelectDate.getText().toString(), temp)){
                Toast.makeText(this, "Thêm món ăn theo ngày thành công!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Lỗi khi thêm món ăn theo ngày!", Toast.LENGTH_SHORT).show();
            }
        } else if (edtSelectDate == view) {
            showDatePicker();
        }
    }

    private void showDatePicker(){
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
                    String date = String.format("%02d-%02d-%d", dayOfMonth, month1 + 1, year1);
                    edtSelectDate.setText(date);
                },
                year, month, day
        );

        datePickerDialog.show();
    }

    @Override
    protected void onDestroy() {
        menuItemDAO.close();
        super.onDestroy();
    }
}