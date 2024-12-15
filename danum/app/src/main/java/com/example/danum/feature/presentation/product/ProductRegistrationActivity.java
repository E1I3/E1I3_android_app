package com.example.danum.feature.presentation.product;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.danum.R;

import java.util.ArrayList;
import java.util.List;

public class ProductRegistrationActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST_CODE = 102;
    private static final int STORAGE_PERMISSION_CODE = 103;

    private Button buttonTrade, buttonShare, saveButton;
    private List<Button> transactionButton = new ArrayList<>();
    private List<Button> productButton = new ArrayList<>();
    private LinearLayout imageUploadLayout;
    private ImageView previewImageView, deleteButton, cameraImageView;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_registration);
        setupToolbar();
        initializeUIElements();
        uploadImage();
        listButtons();

        saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> {
            // TODO: 상품 등록 처리
            Toast.makeText(this, "상품이 등록되었습니다.", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageButton backButton = findViewById(R.id.backButton);
        TextView toolbarTitle = findViewById(R.id.toolbarTitle);

        toolbarTitle.setText("등록");
        backButton.setOnClickListener(v ->{
            finish();
        });
    }

    /**
     * 이미지 업로드 관련 초기화 및 클릭 이벤트 설정
     */
    private void uploadImage() {
        imageUploadLayout = findViewById(R.id.image_upload_layout);
        previewImageView = findViewById(R.id.preview_image);
        deleteButton = findViewById(R.id.image_delete_button);

        // 이미지 업로드 레이아웃 클릭 시 파일 선택
        imageUploadLayout.setOnClickListener(v -> {
            openImageChooser();
        });

        // 삭제 버튼 클릭 시 이미지 제거
        deleteButton.setOnClickListener(v -> deleteSelectedImage());
    }

    /**
     * 저장소 권한 확인
     */
    private boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 저장소 권한 요청
     */
    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    /**
     * 이미지 선택기 열기
     */
    private void openImageChooser() {
        Log.d("카메라 열기", "카메라 열기");
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요"), PICK_IMAGE_REQUEST_CODE);
    }

    /**
     * 이미지 선택 결과 처리
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                Glide.with(this).load(selectedImageUri).into(previewImageView);
                previewImageView.setVisibility(View.VISIBLE);
                deleteButton.setVisibility(View.VISIBLE);
                cameraImageView.setVisibility(View.GONE);
            } else {
                Toast.makeText(this, "이미지를 선택하지 않았습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 선택된 이미지 삭제
     */
    private void deleteSelectedImage() {
        selectedImageUri = null;
        previewImageView.setVisibility(View.GONE);
        deleteButton.setVisibility(View.GONE);
        cameraImageView.setVisibility(View.VISIBLE);
        Toast.makeText(this, "이미지가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
    }

    /**
     * 거래 및 나눔 버튼 초기화
     */
    private void initializeUIElements() {
        buttonTrade = findViewById(R.id.button_trade);
        buttonShare = findViewById(R.id.button_share);

        // 버튼 리스트에 추가
        transactionButton.clear(); // 초기화
        transactionButton.add(buttonTrade);
        transactionButton.add(buttonShare);

        // 클릭 이벤트 설정
        for (Button button : transactionButton) {
            button.setOnClickListener(v -> setActiveTradeButton(button));
        }

        // 초기 활성화 상태 ("거래" 버튼 선택)
        setActiveTradeButton(buttonTrade);
    }
    /**
     * 거래 및 나눔 버튼 활성화 관리
     */
    private void setActiveTradeButton(Button activeButton) {
        for (Button button : transactionButton) {
            if (button == activeButton) {
                // 활성화된 버튼 스타일
                button.setBackground(getResources().getDrawable(R.drawable.button_switch_on)); // 활성화된 상태의 배경
            } else {
                // 비활성화된 버튼 스타일
                button.setBackground(getResources().getDrawable(R.drawable.button_switch_off)); // 비활성화된 상태의 배경
            }
        }
    }
    /**
     * 상품 종류 버튼 리스트 초기화 및 클릭 이벤트 설정
     */
    private void listButtons() {
        Button buttonVegetables = findViewById(R.id.button_vegetables);
        Button buttonFruits = findViewById(R.id.button_fruits);
        Button buttonMeat = findViewById(R.id.button_meat);
        Button buttonSeafood = findViewById(R.id.button_seafood);
        Button buttonDairy = findViewById(R.id.button_dairy);
        Button buttonBakery = findViewById(R.id.button_bakery);

        // 버튼 리스트 초기화
        productButton.clear();
        productButton.add(buttonVegetables);
        productButton.add(buttonFruits);
        productButton.add(buttonMeat);
        productButton.add(buttonSeafood);
        productButton.add(buttonDairy);
        productButton.add(buttonBakery);

        // 클릭 이벤트 설정
        for (Button button : productButton) {
            button.setOnClickListener(v -> setActiveButton(button));
        }

        // 초기 활성화 상태
        setActiveButton(buttonVegetables); // 기본 선택: 채소 버튼
    }

    /**
     * 활성 버튼 설정 메서드 (하나의 버튼만 on)
     */
    private void setActiveButton(Button activeButton) {
        for (Button button : productButton) {
            if (button == activeButton) {
                // 활성화된 버튼 스타일 적용
                button.setBackground(getResources().getDrawable(R.drawable.button_switch_on)); // 활성화된 상태의 배경
            } else {
                // 비활성화된 버튼 스타일 적용
                button.setBackground(getResources().getDrawable(R.drawable.button_switch_off)); // 비활성화된 상태의 배경
            }
        }
    }


}
