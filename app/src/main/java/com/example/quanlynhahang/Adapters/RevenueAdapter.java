package com.example.quanlynhahang.Adapters;

import android.app.Activity;
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
import com.example.quanlynhahang.entity.Revenue;
import com.example.quanlynhahang.features.RevenueDetailActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class RevenueAdapter extends ArrayAdapter<Revenue> {
    private final Activity context;
    private final int layoutID;
    private final ArrayList<Revenue> list;

    public RevenueAdapter(Activity context, int layoutID, ArrayList<Revenue> list) {
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

        Revenue revenue = list.get(position);

        TextView txtRevenueDate = convertView.findViewById(R.id.txtRevenueDate);
        TextView txtRevenueAmount = convertView.findViewById(R.id.txtRevenueAmount);
        ImageButton btnToDetailRevenue = convertView.findViewById(R.id.btnToDetailRevenue);

        txtRevenueDate.setText(revenue.getRevenue_date());

        // Định dạng giá tiền theo chuẩn Việt Nam
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedPrice = decimalFormat.format(revenue.getRevenue_amount()) + "₫";
        txtRevenueAmount.setText(formattedPrice);

        btnToDetailRevenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myItent = new Intent(context, RevenueDetailActivity.class);
                myItent.putExtra("tableId", revenue.getTable_id());
                myItent.putExtra("revenueDate", revenue.getRevenue_date());
                myItent.putExtra("revenueAmount", revenue.getRevenue_amount());
                context.startActivity(myItent);
            }
        });

        return convertView;
    }
}
