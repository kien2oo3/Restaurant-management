package com.example.quanlynhahang.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.quanlynhahang.R;
import com.example.quanlynhahang.entity.MenuItems;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MenuItemForTableAdapter extends ArrayAdapter<MenuItems> {
    private final Activity context;
    private final ArrayList<MenuItems> list;
    private final int layoutID;
    private final Map<MenuItems, Integer> selectedItemsWithQuantity;

    public MenuItemForTableAdapter(Activity context, int layoutID, ArrayList<MenuItems> list) {
        super(context, layoutID, list);
        this.context = context;
        this.layoutID = layoutID;
        this.list = list;
        selectedItemsWithQuantity = new HashMap<>();  // Khởi tạo Map để luu các món va số lượng đã chọn
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(layoutID, null);
        }

        if (!list.isEmpty() && position >= 0) {
            TextView txtTenMonChoBan = convertView.findViewById(R.id.txtTenMonChoBan);
            EditText edtGiaMonChoBan = convertView.findViewById(R.id.edtGiaMonChoBan);
            EditText edtSoLuongChoBan = convertView.findViewById(R.id.edtSoLuongChoBan);
            CheckBox chkChoBan = convertView.findViewById(R.id.chkChoBan);

            MenuItems item = list.get(position);

            if(item!=null){
                txtTenMonChoBan.setText(item.getMenu_item_name());
                // Định dạng giá tiền theo chuẩn Việt Nam
                DecimalFormat decimalFormat = new DecimalFormat("#,###");
                String formattedPrice = decimalFormat.format(item.getMenu_item_price()) + "₫";
                edtGiaMonChoBan.setText(formattedPrice);
            }

            // Kiểm tra trạng thái đã chọn
            chkChoBan.setChecked(selectedItemsWithQuantity.containsKey(item));
            edtSoLuongChoBan.setText(String.valueOf(selectedItemsWithQuantity.getOrDefault(item, 0)));

            // Xử lý sự kiện thay đổi số lượng
            edtSoLuongChoBan.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        int quantity = Integer.parseInt(s.toString());
                        if (chkChoBan.isChecked()) {
                            selectedItemsWithQuantity.put(item, quantity);
                        }
                    } catch (NumberFormatException e) {
                        // Nếu người dùng nhập không phải số, có thể xử lý lỗi ở đây
                        e.printStackTrace();
                    }
                }
            });

            // Xử lý sự kiện chọn/bỏ chọn món
            chkChoBan.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    int quantity = 0;
                    if (!edtSoLuongChoBan.getText().toString().isEmpty()) {
                        quantity = Integer.parseInt(edtSoLuongChoBan.getText().toString());
                    }
                    selectedItemsWithQuantity.put(item, quantity);
                } else {
                    selectedItemsWithQuantity.remove(item);
                }
            });
        }

        return convertView;
    }

    public Map<MenuItems, Integer> getSelectedItemsWithQuantity(){
        return selectedItemsWithQuantity;
    }

    public void setSelectedItemsWithQuantity(Map<MenuItems, Integer> list){
        selectedItemsWithQuantity.clear();
        selectedItemsWithQuantity.putAll(list);
    }

}
