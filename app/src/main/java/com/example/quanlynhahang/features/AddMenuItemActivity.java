package com.example.quanlynhahang.features;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.quanlynhahang.R;
import com.example.quanlynhahang.dao.MenuItemDAO;
import com.example.quanlynhahang.entity.MenuItems;

public class AddMenuItemActivity extends AppCompatActivity implements View.OnClickListener {
    MenuItemDAO menuItemDAO = null;
    EditText edtNewItemName, edtNewItemDesc, edtNewItemPrice;
    ImageView btnNewItemImg;
    Button btnAddMenuItem;
    Toolbar toolbarAddMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_menu_item);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        menuItemDAO = new MenuItemDAO(AddMenuItemActivity.this);

        getWidgets();

        btnNewItemImg.setOnClickListener(this);
        btnAddMenuItem.setOnClickListener(this);
    }

    private void getWidgets() {
        edtNewItemName = findViewById(R.id.edtNewItemName);
        edtNewItemDesc = findViewById(R.id.edtNewItemDesc);
        edtNewItemPrice = findViewById(R.id.edtNewItemPrice);
        btnNewItemImg = findViewById(R.id.btnNewItemImg);
        btnAddMenuItem = findViewById(R.id.btnAddMenuItem);

        toolbarAddMenuItem = findViewById(R.id.toolbarAddMenuItem);
        setSupportActionBar(toolbarAddMenuItem);
        // Hien thi nut back tren menu
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    // Tạo ActivityResultLauncher để chọn ảnh
    private final ActivityResultLauncher<Intent> selectImageLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    if (imageUri != null) {
                        btnNewItemImg.setTag(imageUri.toString()); // Lưu đường dẫn ảnh vào tag
                        Glide.with(this)
                                .load(imageUri)
                                .placeholder(R.drawable.img)
                                .error(R.drawable.error_image)
                                .into(btnNewItemImg);
                    }
                }
            });

    @Override
    public void onClick(View view) {
        if (btnNewItemImg == view) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            selectImageLauncher.launch(intent); // Sử dụng ActivityResultLauncher
        } else if (btnAddMenuItem == view) {
            if (checkEditText()) {
                String name = edtNewItemName.getText().toString();
                String description = edtNewItemDesc.getText().toString();
                int price = Integer.parseInt(edtNewItemPrice.getText().toString());
                String imgPath = (String) btnNewItemImg.getTag();
                MenuItems item = new MenuItems(0, name, description, price, imgPath);
                if (menuItemDAO.addNewMenuItem(item) != -1) {
                    Toast.makeText(this, "Thêm món ăn thành công!", Toast.LENGTH_SHORT).show();
                    resetEditBox();
                } else {
                    Toast.makeText(this, "Lỗi khi thêm món ăn!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private boolean checkEditText() {
        if (edtNewItemName.getText().toString().isEmpty()) {
            edtNewItemName.setError("Không để trống tên món ăn!");
            edtNewItemName.requestFocus();
            return false;
        }
        if (edtNewItemDesc.getText().toString().isEmpty()) {
            edtNewItemDesc.setError("Không để trống mô tả món ăn!");
            edtNewItemDesc.requestFocus();
            return false;
        }
        if (edtNewItemPrice.getText().toString().isEmpty()) {
            edtNewItemPrice.setError("Không để trống giá tiền món ăn!");
            edtNewItemPrice.requestFocus();
            return false;
        }
        return true;
    }

    private  void resetEditBox(){
        edtNewItemName.setText("");
        edtNewItemName.requestFocus();
        edtNewItemDesc.setText("");
        edtNewItemPrice.setText("");
        Glide.with(this)
                .load(R.drawable.img)
                .placeholder(R.drawable.img)
                .error(R.drawable.error_image)
                .into(btnNewItemImg);
    }

    @Override
    protected void onDestroy() {
        menuItemDAO.close();
        super.onDestroy();
    }
}