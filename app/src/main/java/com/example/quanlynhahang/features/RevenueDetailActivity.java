package com.example.quanlynhahang.features;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quanlynhahang.Adapters.RevenueDetailAdapter;
import com.example.quanlynhahang.R;
import com.example.quanlynhahang.dao.RevenueDAO;
import com.example.quanlynhahang.entity.RevenueDetail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class RevenueDetailActivity extends AppCompatActivity {
    TextView txtDetailTableName, txtDetailMenuDate, txtDetailRevenueDate, txtDetailRevenueAmount, txtDetailRevenueId;
    Button btnExportPDF;
    ListView lvDetailMenuItem;
    Toolbar toolbarRevenueDetail;

    RevenueDAO revenueDAO = null;

    ArrayList<RevenueDetail> myArr = null;
    RevenueDetailAdapter adapter = null;

    // Khởi tạo các biến toàn cục dung trong PDF
    private int revenue_id;
    private int table_id;
    private String revenue_date;
    private String revenue_amount;
    private String menu_date;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_revenue_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        revenueDAO = new RevenueDAO(RevenueDetailActivity.this);

        getWidgets();

        Intent myIntent = getIntent();
        if (myIntent != null) {
            int revenueId = myIntent.getIntExtra("revenueId", -1);
            int tableId = myIntent.getIntExtra("tableId", -1);
            String detailDate = myIntent.getStringExtra("revenueDate");
            int detailAmount = myIntent.getIntExtra("revenueAmount", -1);
            if (tableId != -1 && revenueId != -1) {
                myArr = revenueDAO.getAllRevenueDetailByTableID(tableId);
                adapter = new RevenueDetailAdapter(RevenueDetailActivity.this, R.layout.layout_item_chi_tiet_theo_ban, myArr);
                lvDetailMenuItem.setAdapter(adapter);

                txtDetailRevenueId.setText(revenueId + "");
                txtDetailTableName.setText("Bàn số " + myArr.get(0).getDetail_table_name());
                txtDetailMenuDate.setText(myArr.get(0).getDetail_menu_date());
                txtDetailRevenueDate.setText(detailDate);
                // Định dạng giá tiền theo chuẩn Việt Nam
                DecimalFormat decimalFormat = new DecimalFormat("#,###");
                String formattedPrice = decimalFormat.format(detailAmount) + "₫";
                txtDetailRevenueAmount.setText(formattedPrice);

                // Gán giá trị cho các biến toàn cục:
                revenue_id = revenueId;
                table_id = tableId;
                revenue_date = detailDate;
                revenue_amount = formattedPrice;
                menu_date = myArr.get(0).getDetail_menu_date();
            }
        }

        btnExportPDF.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onClick(View view) {
                if (revenue_id != -1 && table_id != -1) {
                    StringBuilder str = new StringBuilder();
                    str.append(String.format("Mã hóa đơn: %d\n", revenue_id));
                    str.append(String.format("Tên bàn: %d\n", table_id));
                    str.append(String.format("Thực đơn ngày: %s\n", menu_date));
                    str.append(String.format("Ngày thanh toán: %s\n", revenue_date));
                    str.append(String.format("Tổng tiền: %s\n", revenue_amount));
                    createInvoicePDF(str.toString());
                } else {
                    Toast.makeText(RevenueDetailActivity.this, "Hóa đơn không tồn tại hoặc đã bị xóa rồi!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void getWidgets() {
        txtDetailTableName = findViewById(R.id.txtDetailTableName);
        txtDetailMenuDate = findViewById(R.id.txtDetailMenuDate);
        lvDetailMenuItem = findViewById(R.id.lvDetailMenuItem);
        txtDetailRevenueDate = findViewById(R.id.txtDetailRevenueDate);
        txtDetailRevenueId = findViewById(R.id.txtDetailRevenueId);
        txtDetailRevenueAmount = findViewById(R.id.txtDetailRevenueAmount);
        btnExportPDF = findViewById(R.id.btnExportPDF);

        toolbarRevenueDetail = findViewById(R.id.toolbarRevenueDetail);
        setSupportActionBar(toolbarRevenueDetail);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void createInvoicePDF(String invoiceDetails) {
        // 1. Tạo đối tượng PdfDocument
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();

        // 2. Tạo trang PDF
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create(); // A4 size
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        // 3. Tạo canvas để vẽ nội dung
        Canvas canvas = page.getCanvas();
        paint.setTextSize(14);

        Paint paintBold = new Paint();
        paintBold.setTextSize(20f);         // Kích thước chữ
        paintBold.setTypeface(Typeface.DEFAULT_BOLD); // In đậm

        // ========================== Vẽ chi tiết hóa đơn có ngắt dòng =====================================
        canvas.drawText("HÓA ĐƠN CHI TIẾT", 180, 50, paintBold);
        String[] lines = invoiceDetails.split("\n"); // Ngắt dòng dựa vào ký tự \n
        int marginLeft = 50; // Lề trái 50px
        int startY = 100; // Vị trí dòng đầu tiên cách mép trên 100px
        int lineHeight = 30; // Khoảng cách giữa các dòng là 30px
        for (String line : lines) {
            canvas.drawText(line, marginLeft, startY, paint); // Vẽ từng dòng với lề trái 100px
            startY += lineHeight; // Di chuyển xuống dòng tiếp theo
        }

        // ========================== Vẽ bảng chi tiết các món =====================================
        // Tiêu đề bảng
        canvas.drawText("CHI TIẾT CÁC MÓN", 200, startY, paint);
        startY += 50;
        canvas.drawText("Tên món", marginLeft, startY, paint);
        canvas.drawText("Giá bán", marginLeft + 200, startY, paint);
        canvas.drawText("Số lượng", marginLeft + 100, startY, paint);
        canvas.drawText("Ngày đặt", marginLeft + 300, startY, paint);

        // Vẽ dòng tiêu đề
        canvas.drawLine(marginLeft, startY + 5, marginLeft + 450, startY + 5, paint);

        // Nội dung bảng
        for (RevenueDetail rd : myArr) {
            startY += lineHeight;

            // Vẽ tên món ăn
            canvas.drawText(rd.getDetail_item_name(), marginLeft, startY, paint);

            // Định dạng giá tiền theo chuẩn Việt Nam
            DecimalFormat decimalFormat = new DecimalFormat("#,###");
            String formattedPrice = decimalFormat.format(rd.getDetail_item_price()) + "₫";
            canvas.drawText(formattedPrice, marginLeft + 200, startY, paint); // Vẽ giá món ăn

            // Vẽ số lượng
            canvas.drawText(rd.getDetail_item_quantity() + "", marginLeft + 100, startY, paint);

            // Vẽ ngày order
            canvas.drawText(rd.getDetail_item_order_date(), marginLeft + 300, startY, paint);

            // Vẽ dòng dưới mỗi sản phẩm
            canvas.drawLine(marginLeft, startY + 5, marginLeft + 450, startY + 5, paint);
        }

        // 4. Kết thúc trang
        pdfDocument.finishPage(page);

        // 5. Lưu PDF vào bộ nhớ
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(directory, "HoaDonChiTiet.pdf");

        try {
            if (file.exists()) {
                file.delete(); // Xóa file cũ nếu tồn tại
            }
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(this, "Hóa đơn đã được xuất: " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Xuất hóa đơn thất bại", Toast.LENGTH_SHORT).show();
        }

        // 6. Đóng PdfDocument
        pdfDocument.close();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        revenueDAO.close();
        super.onDestroy();
    }
}