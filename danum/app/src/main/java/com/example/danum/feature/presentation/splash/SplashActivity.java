package com.example.danum.feature.presentation.splash;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import com.example.danum.R;
import com.example.danum.feature.presentation.login.LoginActivity;

import java.security.MessageDigest;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_DISPLAY_LENGTH = 2000; // 2초 대기 시간

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getAppKeyHash();
        navigateToNextScreen();
    }

    private void navigateToNextScreen() {
        new Handler().postDelayed(this::navigateToLoginScreen, SPLASH_DISPLAY_LENGTH); // 2초 후 이동
    }

    /**
     * 로그인 화면으로 이동
     */
    private void navigateToLoginScreen() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
    }

    /**
     * NOTE: 카카오 로그인 시 필요한 해시키를 얻는 메소드
     */
    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.e("Hash key", something);
            }
        } catch (Exception e) {
            Log.e("name not found", e.toString());
        }
    }
}
