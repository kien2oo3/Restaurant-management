package com.example.quanlynhahang.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.quanlynhahang.R;
import com.example.quanlynhahang.entity.MenuItems;
import com.example.quanlynhahang.features.EditMenuItemActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MenuItemAdapter extends ArrayAdapter<MenuItems> {
    private final Activity context;
    private final int layoutId;
    private final ArrayList<MenuItems> list;

    public MenuItemAdapter(Activity context, int layoutId, ArrayList<MenuItems> list) {
        super(context, layoutId, list);
        this.context = context;
        this.layoutId = layoutId;
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(layoutId, null);
        }

        if (!list.isEmpty() && position >= 0) {
            MenuItems item = list.get(position);

            TextView txtItemName = convertView.findViewById(R.id.txtItemName);
            TextView txtItemPrice = convertView.findViewById(R.id.txtItemPrice);
            ImageView imgItem = convertView.findViewById(R.id.imgItem);
            ImageButton btnToEditMenuItem = convertView.findViewById(R.id.btnToEditMenuItem);

            txtItemName.setText(item.getMenu_item_name());

            // Định dạng giá tiền theo chuẩn Việt Nam
            DecimalFormat decimalFormat = new DecimalFormat("#,###");
            String formattedPrice = decimalFormat.format(item.getMenu_item_price()) + "₫";
            txtItemPrice.setText(formattedPrice);

            // Tải ảnh sử dụng Glide
            String imageUrl = item.getImage();
            Glide.with(context)
                    .load(Uri.parse(imageUrl))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.img)
                    .error(R.drawable.error_image)
                    .into(imgItem);

            // Sự kiện chỉnh sửa món ăn
            btnToEditMenuItem.setOnClickListener(view -> {
                Intent intent = new Intent(context, EditMenuItemActivity.class);
                intent.putExtra("item_id", item.getMenu_item_id());
                context.startActivity(intent);
            });
        }
        return convertView;
    }
}
