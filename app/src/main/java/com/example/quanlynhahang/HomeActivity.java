package com.example.quanlynhahang;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HomeActivity extends AppCompatActivity {
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.actionLogOut) {
            Intent myIntent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(myIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void truyCapQLNV(View v) {
        Intent myItent = new Intent(HomeActivity.this, QuanLyNhanVienActivity.class);
        startActivity(myItent);
    }

    public void truyCapQLMonAn(View v) {
        Intent myItent = new Intent(HomeActivity.this, QuanLyMonAnActivity.class);
        startActivity(myItent);
    }

    public void truyCapMenuTheoNgay(View v) {
        Intent myItent = new Intent(HomeActivity.this, QLMenuTheoNgayActivity.class);
        startActivity(myItent);
    }

    public void truyCapQLBan(View v) {
        Intent myItent = new Intent(HomeActivity.this, QuanLyBanAnActivity.class);
        startActivity(myItent);
    }

    public void truyCapDoanhThu(View v) {
        // Code here
    }
}