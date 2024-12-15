package com.example.danum.feature.presentation.reservation;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.danum.R;
import com.example.danum.core.data.model.Order;

import java.util.ArrayList;
import java.util.List;

public class ReservationHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_history);
        setupToolbar();
        RecyclerView recyclerView = findViewById(R.id.reservationHistoryRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Order> orders = new ArrayList<>();
        orders.add(new Order(R.drawable.carrot, "당근 저렴하게 팝니다.", "계당 400원", 4, "24.12.17 14:00", "대기 중"));
        orders.add(new Order(R.drawable.cabbage, "양파 나눔합니다.", "무료", 8, "24.12.16 14:00", "확정"));
        orders.add(new Order(R.drawable.cabbage, "사과 나눔합니다.", "무료", 4, "24.12.15 14:00", "확정"));
        orders.add(new Order(R.drawable.carrot, "양배추 저렴하게 팝니다.", "계당 1400원", 4, "24.12.15 10:00", "확정"));

        OrderAdapter adapter = new OrderAdapter(this, orders);
        recyclerView.setAdapter(adapter);
    }

    /**
     * 툴바 설정 메서드
     */
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageButton backButton = findViewById(R.id.backButton);
        TextView toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("예약 내역");
        backButton.setOnClickListener(v -> {
            finish();
        });
    }
}