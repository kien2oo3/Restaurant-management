package com.example.quanlynhahang.features;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.quanlynhahang.QuanLyMonAnActivity;
import com.example.quanlynhahang.QuanLyNhanVienActivity;
import com.example.quanlynhahang.R;
import com.example.quanlynhahang.dao.MenuItemDAO;
import com.example.quanlynhahang.entity.MenuItems;

public class EditMenuItemActivity extends AppCompatActivity implements View.OnClickListener {
    private MenuItemDAO menuItemDAO;
    private EditText edtItemID, edtItemName, edtItemDesc, edtItemPrice;
    private ImageView btnItemImg;
    private Button btnDeleteItem, btnEditItem;
    private String currentUrlImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_menu_item);

        menuItemDAO = new MenuItemDAO(this);

        // Lấy thông tin widget
        getWidgets();

        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        if (intent != null) {
            int itemId = intent.getIntExtra("item_id", -1);
            if (itemId != -1) {
                MenuItems item = menuItemDAO.getMenuItemByID(itemId);
                if (item != null) {
                    edtItemID.setText(String.valueOf(item.getMenu_item_id()));
                    edtItemName.setText(item.getMenu_item_name());
                    edtItemDesc.setText(item.getMenu_item_description());
                    edtItemPrice.setText(String.valueOf(item.getMenu_item_price()));
                    currentUrlImg = item.getImage();
                    Glide.with(this)
                            .load(Uri.parse(currentUrlImg))
                            .placeholder(R.drawable.img)
                            .error(R.drawable.error_image)
                            .into(btnItemImg);
                }

            }
        }

        // Sự kiện chọn ảnh mới
        btnItemImg.setOnClickListener(this);

        // Sự kiện chỉnh sửa món ăn
        btnEditItem.setOnClickListener(this);

        // Sự kiện xóa món ăn
        btnDeleteItem.setOnClickListener(this);
    }

    private final ActivityResultLauncher<Intent> selectImageLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    if (imageUri != null) {
                        currentUrlImg = imageUri.toString();
                        Glide.with(EditMenuItemActivity.this)
                                .load(Uri.parse(currentUrlImg))
                                .placeholder(R.drawable.img)
                                .error(R.drawable.error_image)
                                .into(btnItemImg);
                    }
                }
            });

    private void getWidgets() {
        edtItemID = findViewById(R.id.edtItemID);
        edtItemName = findViewById(R.id.edtItemName);
        edtItemDesc = findViewById(R.id.edtItemDesc);
        edtItemPrice = findViewById(R.id.edtItemPrice);
        btnItemImg = findViewById(R.id.btnItemImg);
        btnDeleteItem = findViewById(R.id.btnDeleteItem);
        btnEditItem = findViewById(R.id.btnEditItem);

        Toolbar toolbarEditMenuItem = findViewById(R.id.toolbarEditMenuItem);
        setSupportActionBar(toolbarEditMenuItem);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private boolean checkEditText() {
        if (edtItemName.getText().toString().isEmpty()) {
            edtItemName.setError("Không để trống tên món ăn!");
            edtItemName.requestFocus();
            return false;
        }
        if (edtItemDesc.getText().toString().isEmpty()) {
            edtItemDesc.setError("Không để trống mô tả món ăn!");
            edtItemDesc.requestFocus();
            return false;
        }
        if (edtItemPrice.getText().toString().isEmpty()) {
            edtItemPrice.setError("Không để trống giá tiền món ăn!");
            edtItemPrice.requestFocus();
            return false;
        }
        return true;
    }

    private MenuItems getItem() {
        int item_id = Integer.parseInt(edtItemID.getText().toString());
        String item_name = edtItemName.getText().toString();
        String item_desc = edtItemDesc.getText().toString();
        int item_price = Integer.parseInt(edtItemPrice.getText().toString());
        String item_img = currentUrlImg;
        return new MenuItems(item_id, item_name, item_desc, item_price, item_img);
    }

    @Override
    public void onClick(View view) {
        if (btnItemImg == view) {
            Intent intentPickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            selectImageLauncher.launch(intentPickImage);
        } else if (btnEditItem == view) {
            if (checkEditText()) {
                MenuItems item = getItem();
                if (menuItemDAO.editMenuItemByID(item) > 0) {
                    Toast.makeText(this, "Sửa món ăn thành công!", Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(EditMenuItemActivity.this, QuanLyMonAnActivity.class);
                    startActivity(myIntent);
                } else {
                    Toast.makeText(this, "Lỗi khi sửa món ăn!", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (btnDeleteItem == view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(EditMenuItemActivity.this);
            builder.setTitle("Xóa món ăn");
            builder.setMessage("Bạn chắc chắn muốn xóa?");
            builder.setIcon(R.drawable.icon_delete);
            builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (menuItemDAO.deleteMenuItemByID(Integer.parseInt(edtItemID.getText().toString())) > 0) {
                        Toast.makeText(EditMenuItemActivity.this, "Xóa món ăn thành công!", Toast.LENGTH_SHORT).show();
                        Intent myIntent = new Intent(EditMenuItemActivity.this, QuanLyMonAnActivity.class);
                        startActivity(myIntent);
                    } else {
                        Toast.makeText(EditMenuItemActivity.this, "Lỗi khi xóa món ăn!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            builder.create().show();
        }
    }

    @Override
    protected void onDestroy() {
        menuItemDAO.close();
        super.onDestroy();
    }
}
