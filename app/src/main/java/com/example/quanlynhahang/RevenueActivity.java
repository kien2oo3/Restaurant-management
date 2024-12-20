package com.example.quanlynhahang;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quanlynhahang.Adapters.RevenueAdapter;
import com.example.quanlynhahang.dao.RevenueDAO;
import com.example.quanlynhahang.entity.Revenue;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class RevenueActivity extends AppCompatActivity implements View.OnClickListener {
    ImageButton btnFilterByDate;
    Button btnFilterToDay, btnSendBoss;
    EditText edtShowRevenue, edtSelectDetaildate;
    ListView lvRevenue;
    Toolbar toolbarDoanhThu;

    ArrayList<Revenue> myArr = null;
    RevenueAdapter adapter = null;

    RevenueDAO revenueDAO = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_revenue);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        revenueDAO = new RevenueDAO(RevenueActivity.this);

        getWidgets();

        myArr = revenueDAO.getAllRevenue();
        Collections.reverse(myArr);
        adapter = new RevenueAdapter(RevenueActivity.this, R.layout.layout_item_doanh_thu, myArr);
        lvRevenue.setAdapter(adapter);

        edtSelectDetaildate.setOnClickListener(this);
        btnFilterByDate.setOnClickListener(this);
        btnSendBoss.setOnClickListener(this);
        btnFilterToDay.setOnClickListener(this);
    }

    private void getWidgets() {
        lvRevenue = findViewById(R.id.lvRevenue);
        btnFilterByDate = findViewById(R.id.btnFilterByDate);
        btnFilterToDay = findViewById(R.id.btnFilterToDay);
        btnSendBoss = findViewById(R.id.btnSendBoss);
        edtShowRevenue = findViewById(R.id.edtShowRevenue);
        edtSelectDetaildate = findViewById(R.id.edtSelectDetaildate);

        toolbarDoanhThu = findViewById(R.id.toolbarDoanhThu);
        setSupportActionBar(toolbarDoanhThu);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onClick(View view) {
        if (edtSelectDetaildate == view) {
            showDatePicker();
        } else if (btnFilterByDate == view) {
            if (edtSelectDetaildate.getText().toString().isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn ngày để tìm!", Toast.LENGTH_LONG).show();
            } else {
                int total = 0;
                String dateFilter = edtSelectDetaildate.getText().toString();
                if (myArr != null) {
                    total = getTotalByDate(dateFilter, myArr);
                }
                // Định dạng giá tiền theo chuẩn Việt Nam
                DecimalFormat decimalFormat = new DecimalFormat("#,###");
                String formattedPrice = decimalFormat.format(total) + "₫";
                edtShowRevenue.setText(String.format("Ngày %s, doanh thu: %s", dateFilter, formattedPrice));
            }

        } else if (btnFilterToDay == view) {
            // Lấy ngày giờ hiện tại bằng Calendar
            Calendar calendar = Calendar.getInstance();
            // Định dạng ngày giờ theo "dd-MM-yyyy HH:mm:ss"
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            String formattedDateTime = formatter.format(calendar.getTime());
            int total = 0;
            if (myArr != null) {
                total = getTotalByDate(formattedDateTime, myArr);
            }
            // Định dạng giá tiền theo chuẩn Việt Nam
            DecimalFormat decimalFormat = new DecimalFormat("#,###");
            String formattedPrice = decimalFormat.format(total) + "₫";
            edtShowRevenue.setText(String.format("Ngày %s, doanh thu: %s", formattedDateTime, formattedPrice));
        } else if (btnSendBoss == view) {
            if(edtShowRevenue.getText().toString().isEmpty()){
                edtSelectDetaildate.setError("Tìm doanh thu theo ngày để gửi!");
                return;
            }
            edtSelectDetaildate.setError(null);
            sendEmail();
        }
    }

    private void showDatePicker() {
        // Khởi tạo đối tượng Calendar
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Tạo và hiển thị DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year1, month1, dayOfMonth) -> {
                    // Định dạng ngày theo kiểu "dd-MM-yyyy"
                    String date = String.format("%02d-%02d-%04d", dayOfMonth, month1 + 1, year1);
                    edtSelectDetaildate.setText(date);
                },
                year, month, day
        );

        datePickerDialog.show();
    }

    private int getTotalByDate(String dateFilter, ArrayList<Revenue> list) {
        int total = 0;
        for (Revenue rv : list) {
            String[] temp = rv.getRevenue_date().split(" ");
            if (temp[0].equals(dateFilter)) {
                total += rv.getRevenue_amount();
            }
        }

        return total;
    }

    public void sendEmail() {
        Intent email = new Intent(Intent.ACTION_SEND);
        String to1 = "duongvt01@gmail.com";
        String to2 = "haui@gmail.com";
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{to1, to2});//to
        email.putExtra(Intent.EXTRA_SUBJECT, "Báo cáo doanh thu theo ngày:");//subject
        email.putExtra(Intent.EXTRA_TEXT, edtShowRevenue.getText() + "");//body

        //need this to prompts email client only
        email.setType("message/rfc822");

        startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_revenue_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        assert searchView != null;
        // Gán sự kiện submit:
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String date) {
                if (date.isEmpty()) {
                    return false;
                }
                ArrayList<Revenue> revenueArrayList = new ArrayList<>();
                for (Revenue rv : myArr) {
                    if (rv.getRevenue_date().split(" ")[0].equals(date)) {
                        revenueArrayList.add(rv);
                    }
                }
                if (revenueArrayList.size() <= 0) {
                    Toast.makeText(RevenueActivity.this, "Không tìm thấy doanh thu ngày " + date, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RevenueActivity.this, String.format("Đã tìm được %d bản ghi", revenueArrayList.size()), Toast.LENGTH_SHORT).show();
                }
                adapter = new RevenueAdapter(RevenueActivity.this, R.layout.layout_item_doanh_thu, revenueArrayList);
                lvRevenue.setAdapter(adapter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        revenueDAO.close();
        super.onDestroy();
    }
}