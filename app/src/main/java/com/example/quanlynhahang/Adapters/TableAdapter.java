package com.example.quanlynhahang.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.quanlynhahang.entity.Tables;
import com.example.quanlynhahang.R;

import java.util.ArrayList;


public class TableAdapter extends ArrayAdapter<Tables> {
    private final Activity context;
    private final int layoutID;
    private final ArrayList<Tables> list;

    public TableAdapter(Activity context, int layoutID, ArrayList<Tables> list) {
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

        Tables table = list.get(position);

        TextView txtTenBan = convertView.findViewById(R.id.txtTenBan);
        TextView txtTrangThai = convertView.findViewById(R.id.txtTrangThai);

        txtTenBan.setText("Bàn " + table.getTable_number());
        txtTrangThai.setText("Trạng thái: "+table.getStatus());

        return convertView;
    }
}
