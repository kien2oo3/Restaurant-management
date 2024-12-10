package com.example.quanlynhahang.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.quanlynhahang.R;
import com.example.quanlynhahang.entity.Employees;
import com.example.quanlynhahang.entity.Employees;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class EmployeeAdapter extends ArrayAdapter<Employees> {
    private final Activity context;
    private final ArrayList<Employees> list;
    private final int layoutID;

    public EmployeeAdapter(Activity context, int layoutID, ArrayList<Employees> list) {
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
            TextView txtFullName = convertView.findViewById(R.id.txtFullName);
            TextView txtPosition = convertView.findViewById(R.id.txtPosition);
            TextView txtSalary = convertView.findViewById(R.id.txtSalary);

            Employees e = list.get(position);
            txtFullName.setText(e.getFull_name());
            txtPosition.setText(e.getPosition());

            // Định dạng giá tiền theo chuẩn Việt Nam
            DecimalFormat decimalFormat = new DecimalFormat("#,###");
            String formattedPrice = decimalFormat.format(e.getSalary()) + "₫";
            txtSalary.setText(formattedPrice);
        }
        return convertView;
    }
}
