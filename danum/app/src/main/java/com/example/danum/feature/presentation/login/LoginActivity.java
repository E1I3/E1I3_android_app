package com.example.danum.feature.presentation.login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.danum.R;
import com.example.danum.core.config.KakaoAuthClient;
import com.example.danum.core.data.model.UserProfile;
import com.example.danum.core.util.UserSessionManager;
import com.example.danum.feature.presentation.home.HomeActivity;
import com.example.danum.feature.utils.RequestPermissionsUtil;
import com.kakao.sdk.user.UserApiClient;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginActivity extends ComponentActivity {
    private static final int PERMISSION_REQUEST_CODE = 100; // 권한 요청 코드
    private static final String TAG = "카카오 로그인 액티비티";
    private RequestPermissionsUtil permissionsUtil;
    private KakaoAuthClient kakaoAuthClient;
    private UserSessionManager sessionManager;
    private ImageButton kakaoLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        kakaoAuthClient = new KakaoAuthClient();
        sessionManager = new UserSessionManager(this);
        permissionsUtil = new RequestPermissionsUtil(this);
        requestPermissionsIfNeeded();

        permissionsUtil.requestLocationPermissions(this);
        setupEventListeners();
    }

    private void requestPermissionsIfNeeded() {
        requestPermissions();
    }

    private void requestPermissions() {
        String[] permissions = new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions = new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.POST_NOTIFICATIONS
            };
        }

        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
    }

    private void setupEventListeners() {
        kakaoLoginButton = findViewById(R.id.kakao_login_button);

        kakaoLoginButton.setOnClickListener(v -> {
            kakaoAuthClient.loginWithKakaoTalk(
                    LoginActivity.this,
                    this::handleLoginSuccess,
                    this::handleLoginFailure
            );
        });
    }

    private void handleLoginSuccess(String accessToken) {
        Log.d(TAG, "카카오 로그인 성공. 토큰: " + accessToken);
        Toast.makeText(this, "카카오 로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show();

        UserApiClient.getInstance().me((user, error) -> {
            if (error != null) {
                handleLoginFailure(error);
                Log.d(TAG, "카카오 사용자 정보 가져오기 실패", error);
                return null;
            }
            launchHomeActivity();
            return null;
        });
    }

    private void handleLoginFailure(Throwable error) {
        Log.e(TAG, "카카오 로그인 실패", error);
        Toast.makeText(this, "카카오 로그인 실패: " + error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void launchHomeActivity() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }
}
