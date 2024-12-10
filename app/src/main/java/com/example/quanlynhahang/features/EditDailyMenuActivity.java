package com.example.quanlynhahang.features;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quanlynhahang.Adapters.DailyMenuItemAdapter;
import com.example.quanlynhahang.R;
import com.example.quanlynhahang.dao.DailyMenuDAO;
import com.example.quanlynhahang.dao.MenuItemDAO;
import com.example.quanlynhahang.entity.MenuItems;

import java.util.ArrayList;

public class EditDailyMenuActivity extends AppCompatActivity {
    EditText edtDate;
    Button btnFeature;
    ListView lvMonAnByDate;
    Toolbar toolbarEditMonAnTheoNgay;

    DailyMenuDAO dailyMenuDAO = null;
    MenuItemDAO menuItemDAO = null;
    ArrayList<MenuItems> myArr = null;
    DailyMenuItemAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_daily_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        menuItemDAO = new MenuItemDAO(EditDailyMenuActivity.this);
        dailyMenuDAO = new DailyMenuDAO(EditDailyMenuActivity.this);

        getWidgets();

        registerForContextMenu(btnFeature);

        myArr = menuItemDAO.getAllMenuItem();
        adapter = new DailyMenuItemAdapter(EditDailyMenuActivity.this, R.layout.layout_item_mon_theo_ngay, myArr);

        Intent myIntent = getIntent();
        if(myIntent!=null){
            String date = myIntent.getStringExtra("mydate");
            if(date!=null){
                edtDate.setText(date);
                ArrayList<MenuItems> list_itemSelected = dailyMenuDAO.getItemsIDByDate(date);
                Toast.makeText(this, "Kích thước là: "+list_itemSelected.size(), Toast.LENGTH_LONG).show();
                if(!list_itemSelected.isEmpty()){
                    adapter.setSelectedItems(list_itemSelected);
                }
            }
        }
        lvMonAnByDate.setAdapter(adapter);

    }

    private void getWidgets() {
        edtDate = findViewById(R.id.edtDate);
        btnFeature = findViewById(R.id.btnFeature);
        lvMonAnByDate = findViewById(R.id.lvMonAnByDate);

        toolbarEditMonAnTheoNgay = findViewById(R.id.toolbarEditMonAnTheoNgay);
        setSupportActionBar(toolbarEditMonAnTheoNgay);
        // hien thi nut back tren menu
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.actionSua) {

            return true;
        } else if (id == R.id.actionXoa) {

            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        menuItemDAO.close();
        super.onDestroy();
    }
}