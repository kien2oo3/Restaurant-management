package com.example.quanlynhahang.features;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quanlynhahang.Adapters.MenuItemForTableAdapter;
import com.example.quanlynhahang.R;
import com.example.quanlynhahang.dao.MenuItemDAO;
import com.example.quanlynhahang.dao.TableDAO;
import com.example.quanlynhahang.dao.TableMenuItemDAO;
import com.example.quanlynhahang.entity.MenuItems;
import com.example.quanlynhahang.entity.TableMenuItems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UpdateMenuItemForTableActivity extends AppCompatActivity {
    Button btnUpdateMon;
    ListView lvMonTheoBan;
    Toolbar toolbarUpdateMonTheoBanAn;

    TableMenuItemDAO tableMenuItemDAO = null;
    MenuItemDAO menuItemDAO = null;

    ArrayList<MenuItems> myArr = null;
    MenuItemForTableAdapter adapter = null;

    private int tableId;
    private String tableDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_menu_item_for_table);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tableMenuItemDAO = new TableMenuItemDAO(UpdateMenuItemForTableActivity.this);
        menuItemDAO = new MenuItemDAO(UpdateMenuItemForTableActivity.this);

        getWidgets();

        Intent myItent = getIntent();
        if(myItent!=null){
            tableId = myItent.getIntExtra("table_id", -1);
            tableDate = myItent.getStringExtra("table_date");
        }

        myArr = menuItemDAO.getAllMenuItemByMenuDate(tableDate);
        adapter = new MenuItemForTableAdapter(UpdateMenuItemForTableActivity.this, R.layout.layout_item_mon_theo_ban, myArr);

        ArrayList<TableMenuItems> tableMenuItems = tableMenuItemDAO.getTableMenuItemsByTableId(tableId);
        Toast.makeText(this, "Size: "+tableMenuItems.size(), Toast.LENGTH_SHORT).show();
        Map<MenuItems, Integer> listSelected = new HashMap<>();
        for(TableMenuItems table : tableMenuItems){
            if(table.getTable_id() == tableId){
                MenuItems item = menuItemDAO.getMenuItemByID(table.getMenu_item_id());
                int quantity = table.getQuantity();
                listSelected.put(item, quantity);
            }
        }
        adapter.setSelectedItemsWithQuantity(listSelected);

        lvMonTheoBan.setAdapter(adapter);

        btnUpdateMon.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                Map<MenuItems, Integer> listSelected = adapter.getSelectedItemsWithQuantity();
                if(tableMenuItemDAO.insertTableMenuItems(listSelected, tableId)){
                    Toast.makeText(UpdateMenuItemForTableActivity.this, "Cập nhật các món theo bàn thành công!", Toast.LENGTH_SHORT).show();
                    if(myItent!=null){
                        myItent.putExtra("total", tableMenuItemDAO.getTotalBySelectedNewItems(listSelected));
                        setResult(Activity.RESULT_OK, myItent);
                        finish();
                    }
                }else{
                    Toast.makeText(UpdateMenuItemForTableActivity.this, "Lỗi khi cập nhật các món theo bàn!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getWidgets() {
        btnUpdateMon = findViewById(R.id.btnUpdateMon);
        lvMonTheoBan = findViewById(R.id.lvMonTheoBan);

        toolbarUpdateMonTheoBanAn = findViewById(R.id.toolbarUpdateMonTheoBanAn);
        setSupportActionBar(toolbarUpdateMonTheoBanAn);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        menuItemDAO.close();
        super.onDestroy();
    }
}