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

public class MainActivity extends AppCompatActivity {
    EditText edtUsername, edtPassword;
    Button btnLogin, btnToRegis;
    UserDAO userDAO = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userDAO = new UserDAO(MainActivity.this);

        // Lay cac bien dieu khien
        getWidgets();

        // Gan su kien onCLick
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Kiem tra username rong
                String username = edtUsername.getText().toString();
                if (username.isEmpty()) {
                    edtUsername.requestFocus();
                    edtUsername.setError("Vui lòng nhập tên tài khoản của bạn!");
                    return;
                }
                // Kiem tra password rong
                String password = edtPassword.getText().toString();
                if (password.isEmpty()) {
                    edtPassword.requestFocus();
                    edtPassword.setError("Vui lòng nhập mật khẩu của bạn!");
                    return;
                }
                User user = new User(username, password);
                // Kiem tra tai khoan nguoi dung co ton tai khong
                if (userDAO.checkUser(user)) {
                    Intent myIntent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(myIntent);
                } else {
                    Toast.makeText(MainActivity.this, "Tên đăng nhập hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnToRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(myIntent);
            }
        });

        // Lay Intent tu form dang ky
        getIntentFromRegisForm();
    }

    private void getIntentFromRegisForm() {
        Intent myItent = getIntent();
        if (myItent != null) {
            edtUsername.setText(myItent.getStringExtra("username"));
            edtPassword.setText(myItent.getStringExtra("password"));
        }
    }

    private void getWidgets() {
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnToRegis = findViewById(R.id.btnToRegis);
    }

    @Override
    protected void onDestroy() {
        userDAO.close();
        super.onDestroy();
    }

}