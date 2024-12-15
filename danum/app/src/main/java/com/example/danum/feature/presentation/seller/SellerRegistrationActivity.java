package com.example.danum.feature.presentation.seller;

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

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.danum.R;

public class SellerRegistrationActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST_CODE = 102;
    private static final int STORAGE_PERMISSION_CODE = 103;

    private TextView image_upload_text;
    private ImageView previewImageView, deleteButton, cameraImageView;
    private Uri selectedImageUri;
    private LinearLayout imageUploadLayout;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registration);

        setupToolbar();
        uploadImage();
    }

    /**
     * 툴바 설정 메서드
     */
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageButton backButton = findViewById(R.id.backButton);
        TextView toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("판매자 정보 입력");
        backButton.setOnClickListener(v -> {
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
        image_upload_text = findViewById(R.id.image_upload_text);
        cameraImageView = findViewById(R.id.cameraImageView);
        // 이미지 업로드 레이아웃 클릭 시 파일 선택
        imageUploadLayout.setOnClickListener(v -> {
            openImageChooser();
        });

        // 삭제 버튼 클릭 시 이미지 제거
        deleteButton.setOnClickListener(v -> deleteSelectedImage());

        saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> {
            Toast.makeText(this, "판매자 정보가 등록되었습니다.", Toast.LENGTH_SHORT).show();
            finish();
        });
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
                image_upload_text.setVisibility(View.GONE);
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
        image_upload_text.setVisibility(View.VISIBLE);
        Toast.makeText(this, "이미지가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
    }

}