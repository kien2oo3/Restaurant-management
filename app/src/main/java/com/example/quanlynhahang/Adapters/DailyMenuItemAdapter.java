package com.example.quanlynhahang.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.quanlynhahang.entity.DailyMenuItems;
import com.example.quanlynhahang.R;
import com.example.quanlynhahang.entity.MenuItems;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DailyMenuItemAdapter extends ArrayAdapter<MenuItems> {
    private final Activity context;
    private final ArrayList<MenuItems> list;
    private final int layoutID;
    private Set<MenuItems> selectedItems;

    public DailyMenuItemAdapter(Activity context, int layoutID, ArrayList<MenuItems> list) {
        super(context, layoutID, list);
        this.context = context;
        this.layoutID = layoutID;
        this.list = list;
        selectedItems = new HashSet<>();  // Khởi tạo Set để quanr lý các món đã chọn
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(layoutID, null);
        }
        if (!list.isEmpty() && position >= 0) {
            CheckBox chkItem;
            TextView txtTenMon, txtGiaTien;

            chkItem = convertView.findViewById(R.id.chkItem);
            txtTenMon = convertView.findViewById(R.id.txtTenMon);
            txtGiaTien = convertView.findViewById(R.id.txtGiaTien);

            MenuItems item = list.get(position);
            txtTenMon.setText(item.getMenu_item_name());

            // Định dạng giá tiền theo chuẩn Việt Nam
            DecimalFormat decimalFormat = new DecimalFormat("#,###");
            String formattedPrice = decimalFormat.format(item.getMenu_item_price()) + "₫";
            txtGiaTien.setText(formattedPrice);

            // Nếu check đã được chọn:
            chkItem.setChecked(selectedItems.contains(item));

            // Kiểm tra sự kiện check và uncheck
            chkItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        selectedItems.add(item);
                    }else{
                        selectedItems.remove(item);
                    }
                }
            });


        }
        return convertView;
    }

    // Lấy ra danh sách các item được chọn:
    public Set<MenuItems> getSelectedItems(){
        return selectedItems;
    }
    public void setSelectedItems(ArrayList<MenuItems> myList){
        selectedItems.addAll(myList);
    }
}
