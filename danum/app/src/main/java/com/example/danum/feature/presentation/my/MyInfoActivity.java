package com.example.danum.feature.presentation.my;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.animation.ObjectAnimator;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.danum.R;
import com.example.danum.feature.presentation.reservation.ReservationHistoryActivity;
import com.example.danum.feature.presentation.seller.SellerRegistrationActivity;

public class MyInfoActivity extends AppCompatActivity {
    private View switchSelector;
    private TextView sellerText, userText;
    private boolean isSellerSelected = false; // 기본값: 사용자 선택
    private float switchWidth = 0f; // 전체 스위치의 너비 (패딩 제외)
    private float switchSelectorWidth = 0f; // 선택자 스위치의 너비

    private Button btnSellerRegister, btnReservationCheck;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        setupToolbar();
        initializeSwitch();
        initializeButton();
    }

    private void initializeButton() {
        btnSellerRegister = findViewById(R.id.btnSellerRegister);
        btnSellerRegister.setOnClickListener(v -> {
            // 판매자 등록 화면으로 이동
            Intent intent = new Intent(this, SellerRegistrationActivity.class);
            startActivity(intent);

        });

        btnReservationCheck = findViewById(R.id.btnReservationCheck);
        btnReservationCheck.setOnClickListener(v -> {
            // 예약 확인 화면으로 이동
            Intent intent = new Intent(this, ReservationHistoryActivity.class);
            startActivity(intent);
        });
    }

    /**
     * 툴바 설정 메서드
     */
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageButton backButton = findViewById(R.id.backButton);
        TextView toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("마이페이지");
        backButton.setOnClickListener(v -> {
            finish();
        });
    }

    /**
     * 스위치 초기화 메서드
     */
    private void initializeSwitch() {
        switchSelector = findViewById(R.id.switchSelector);
        sellerText = findViewById(R.id.userTextView);
        userText = findViewById(R.id.sellerTextView);

        // 레이아웃이 완료된 후에 스위치의 너비를 측정
        switchSelector.post(() -> {
            View parent = (View) switchSelector.getParent(); // FrameLayout
            int paddingLeft = parent.getPaddingLeft();
            int paddingRight = parent.getPaddingRight();
            switchWidth = parent.getWidth() - paddingLeft - paddingRight; // available width
            switchSelectorWidth = switchSelector.getWidth(); // 80dp in pixels

            // 초기 위치 설정
            float initialTranslationX = isSellerSelected ? 0 : (switchWidth - switchSelectorWidth);
            switchSelector.setTranslationX(initialTranslationX);

            // switchSelector를 최상위로 설정하여 텍스트에 가려지지 않도록 함
            switchSelector.bringToFront();

            // 초기 텍스트 색상 설정
            updateTextColors();
        });

        // 텍스트 클릭 리스너 설정
        sellerText.setOnClickListener(v -> toggleSwitch(true));
        userText.setOnClickListener(v -> toggleSwitch(false));
    }

    /**
     * 스위치 토글 메서드
     *
     * @param selectSeller 판매자 선택 여부
     */
    private void toggleSwitch(boolean selectSeller) {
        // 이미 선택된 상태라면 아무 동작도 하지 않음
        if (isSellerSelected == selectSeller) {
            return;
        }

        isSellerSelected = selectSeller;
        Log.d("MyInfoActivity", "toggleSwitch: " + selectSeller);

        // 목표 위치 계산 (왼쪽: 0, 오른쪽: switchWidth - switchSelectorWidth)
        float targetTranslationX = selectSeller ? 0 : (switchWidth - switchSelectorWidth);

        // 애니메이션 설정
        ObjectAnimator animator = ObjectAnimator.ofFloat(switchSelector, "translationX", targetTranslationX);
        animator.setDuration(100); // 애니메이션 지속 시간 300ms
        animator.start();

        // switchSelector를 최상위로 설정하여 애니메이션 도중 가려지지 않도록 함
        switchSelector.bringToFront();

        // 텍스트 색상 변경
        updateTextColors();
    }

    /**
     * 텍스트 색상 업데이트 메서드
     */
    private void updateTextColors() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(sellerText, "alpha", 1f, 1f);
        animator.setDuration(100);
        animator.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                // 애니메이션 종료 시 텍스트 색상 변경
                if (isSellerSelected) {
                    sellerText.setTextColor(getResources().getColor(R.color.white));
                    userText.setTextColor(getResources().getColor(R.color.text_gray));
                } else {
                    sellerText.setTextColor(getResources().getColor(R.color.text_gray));
                    userText.setTextColor(getResources().getColor(R.color.white));
                }
            }
        });
        animator.start();
    }

}
