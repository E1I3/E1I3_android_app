package com.example.danum.feature.presentation.product;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.danum.R;
import com.example.danum.core.data.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        setupToolbar();
        initializeUIElements();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageButton backButton = findViewById(R.id.backButton);
        TextView toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setVisibility(View.GONE);
        backButton.setOnClickListener(v -> {
            finish();
        });
    }

    private void initializeUIElements() {
        RecyclerView recyclerView = findViewById(R.id.productRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Product> productList = new ArrayList<>();
        productList.add(new Product(1, "당근 10개(개당 200g)", 800, 10, R.drawable.carrot));
        productList.add(new Product(2, "감자 8개(개당 100g)", 400, 8, R.drawable.potato));
        productList.add(new Product(3, "브로콜리 12개(개당 300g)", 600, 9, R.drawable.broccoli));

        ProductAdapter adapter = new ProductAdapter(this, productList);
        recyclerView.setAdapter(adapter);

        LinearLayout buttonLayout = findViewById(R.id.button_layout);
        Button transactionButton = findViewById(R.id.transectionButton);
        Button sharingButton = findViewById(R.id.sharingButton);

        // 초기 선택 상태 설정 (예: 거래 버튼 활성화)
        setButtonSelected(transactionButton, sharingButton);

        // 거래 버튼 클릭 리스너 설정
        transactionButton.setOnClickListener(v -> {
            setButtonSelected(transactionButton, sharingButton);
        });

        // 나눔 버튼 클릭 리스너 설정
        sharingButton.setOnClickListener(v -> {
            setButtonSelected(sharingButton, transactionButton);
        });

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> {
            Toast.makeText(this, "예약이 완료되었습니다.", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void setButtonSelected(Button selectedButton, Button unselectedButton) {
        // 선택된 버튼 스타일 적용
        selectedButton.setBackgroundResource(R.drawable.button_fragment_on);
        selectedButton.setTextColor(getResources().getColor(R.color.black));
        selectedButton.setTextSize(14);

        // 선택되지 않은 버튼 스타일 적용
        unselectedButton.setBackgroundResource(R.drawable.button_fragment_off);
        unselectedButton.setTextColor(getResources().getColor(R.color.text_gray));
        unselectedButton.setTextSize(14);
    }
}
