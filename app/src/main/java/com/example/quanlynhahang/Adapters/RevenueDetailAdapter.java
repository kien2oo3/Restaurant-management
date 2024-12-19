package com.example.quanlynhahang.Adapters;

import android.annotation.SuppressLint;
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
import com.example.quanlynhahang.entity.RevenueDetail;
import com.example.quanlynhahang.features.RevenueDetailActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class RevenueDetailAdapter extends ArrayAdapter<RevenueDetail> {
    private final Activity context;
    private final int layoutID;
    private final ArrayList<RevenueDetail> list;

    public RevenueDetailAdapter(Activity context, int layoutID, ArrayList<RevenueDetail> list) {
        super(context, layoutID, list);
        this.context = context;
        this.layoutID = layoutID;
        this.list = list;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(layoutID, null);
        }

        RevenueDetail revenueDetail = list.get(position);

        TextView txtDetailItemName, txtDetailItemPrice, txtDetailItemQuantity, txtDetailItemOrderDate;
        txtDetailItemName = convertView.findViewById(R.id.txtDetailItemName);
        txtDetailItemPrice = convertView.findViewById(R.id.txtDetailItemPrice);
        txtDetailItemQuantity = convertView.findViewById(R.id.txtDetailItemQuantity);
        txtDetailItemOrderDate = convertView.findViewById(R.id.txtDetailItemOrderDate);

        txtDetailItemName.setText(revenueDetail.getDetail_item_name());

        // Định dạng giá tiền theo chuẩn Việt Nam
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedPrice = decimalFormat.format(revenueDetail.getDetail_item_price()) + "₫";
        txtDetailItemPrice.setText(formattedPrice);

        txtDetailItemQuantity.setText(revenueDetail.getDetail_item_quantity()+"");
        txtDetailItemOrderDate.setText(revenueDetail.getDetail_item_order_date());

        return convertView;
    }
}
