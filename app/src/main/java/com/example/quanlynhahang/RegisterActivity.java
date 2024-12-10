package com.example.quanlynhahang;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quanlynhahang.dao.UserDAO;
import com.example.quanlynhahang.entity.User;

public class RegisterActivity extends AppCompatActivity {
    EditText edtUsername, edtPassword;
    Button btnRegister, btnToLogin;
    UserDAO userDAO = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userDAO = new UserDAO(RegisterActivity.this);

        // lay cac dieu khien
        getWidgets();

        // Gan su kien onCLick
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Kiem tra username rong
                String username = edtUsername.getText().toString();
                if (username.isEmpty()) {
                    edtUsername.requestFocus();
                    edtUsername.setError("Vui lòng nhập tên tài khoản!");
                    return;
                }
                // Kiem tra password rong
                String password = edtPassword.getText().toString();
                if (password.isEmpty()) {
                    edtPassword.requestFocus();
                    edtPassword.setError("Vui lòng nhập mật khẩu!");
                    return;
                }
                User user = new User(username, password);
                // Kiem tra tai khoan nguoi dung co ton tai khong
                if (!userDAO.checkUserForUsername(user)) {
                    if (userDAO.addNewUser(user) != -1) {
                        Intent myIntent = new Intent(RegisterActivity.this, MainActivity.class);
                        myIntent.putExtra("username", user.getUsername());
                        myIntent.putExtra("password", user.getPassword());
                        Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                        startActivity(myIntent);
                    } else {
                        Toast.makeText(RegisterActivity.this, "Lỗi, đăng ký không thành công!", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(RegisterActivity.this, "Tài khoản người dùng đã tồn tại!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(myIntent);
            }
        });
    }

    private void getWidgets() {
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnToLogin = findViewById(R.id.btnToLogin);
    }

    @Override
    protected void onDestroy() {
        userDAO.close();
        super.onDestroy();
    }
}