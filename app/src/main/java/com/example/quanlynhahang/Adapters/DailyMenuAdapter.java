package com.example.quanlynhahang.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.quanlynhahang.R;
import com.example.quanlynhahang.dao.DailyMenuDAO;
import com.example.quanlynhahang.entity.DailyMenu;
import com.example.quanlynhahang.entity.Employees;
import com.example.quanlynhahang.features.EditDailyMenuActivity;
import com.example.quanlynhahang.features.EditMenuItemActivity;

import java.util.ArrayList;
import java.util.List;

public class DailyMenuAdapter extends ArrayAdapter<DailyMenu> {

    private final Activity context;
    private final ArrayList<DailyMenu> list;
    private final int layoutID;

    public DailyMenuAdapter(Activity context, int layoutID, ArrayList<DailyMenu> list) {
        super(context, layoutID, list);
        this.context = context;
        this.layoutID = layoutID;
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(layoutID, null);
        }
        if (!list.isEmpty() && position >= 0) {
            DailyMenu menu = list.get(position);
            DailyMenuDAO dailyMenuDAO = new DailyMenuDAO(context);
            TextView txtDate, txtSLMon;
            ImageButton btnToEditNgay;

            txtDate = convertView.findViewById(R.id.txtDate);
            txtSLMon = convertView.findViewById(R.id.txtSLMon);
            btnToEditNgay = convertView.findViewById(R.id.btnToEditNgay);

            txtDate.setText(menu.getMenu_date());
            txtSLMon.setText(dailyMenuDAO.getTotalItemByDate(menu.getMenu_date())+" món");

            btnToEditNgay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myItent = new Intent(context, EditDailyMenuActivity.class);
                    String date = menu.getMenu_date();
                    myItent.putExtra("mydate", date);
                    context.startActivity(myItent);
                }
            });

            dailyMenuDAO.close();
        }
        return convertView;
    }
}
