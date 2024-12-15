package com.example.danum.app;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import com.kakao.sdk.common.KakaoSdk;
import com.example.danum.BuildConfig;
import com.kakao.vectormap.KakaoMapSdk;

import androidx.annotation.NonNull;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.io.PrintWriter;
import java.io.StringWriter;

import dagger.hilt.android.HiltAndroidApp;
import lombok.Getter;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * MainApplication 클래스로, 앱의 전역 설정 및 초기화를 담당합니다.
 */
@HiltAndroidApp
public class MainApplication extends Application {

    @Getter
    private static MainApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initLogging();
        initNetworkMonitoring();
        initGlobalExceptionHandler();

        Log.d("다눔 애플리케이션", "카카오 SDK 초기화.");
        KakaoSdk.init(this, BuildConfig.KAKAO_APP_KEY);
        setKakaoMapAppKey();
    }

    private void setKakaoMapAppKey() {
        try {
            // ApplicationInfo 가져오기
            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);

            // meta-data 값 설정
            if (appInfo.metaData != null) {
                KakaoMapSdk.init(this, BuildConfig.KAKAO_APP_KEY);
                appInfo.metaData.putString("com.kakao.vectormap.APP_KEY", BuildConfig.KAKAO_APP_KEY);
                Log.d("com.kakao.vectormap.APP_KEY 카카오", BuildConfig.KAKAO_APP_KEY);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("MainApplication", "ApplicationInfo를 가져올 수 없습니다.", e);
        }
    }

    // 네트워크 로깅 초기화
    private void initLogging() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(loggingInterceptor);
        Log.d("다눔 애플리케이션", "네트워크 로그 초기화.");
    }

    private void initNetworkMonitoring() {
        Log.d("다눔 애플리케이션", "네트워크 모니터링 초기화.");
    }

    private void initGlobalExceptionHandler() {
        final Thread.UncaughtExceptionHandler defaultHandler = Thread.getDefaultUncaughtExceptionHandler();

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(@NonNull Thread thread, @NonNull Throwable throwable) {
                Log.e("다눔 애플리케이션", "스레드 이름: " + thread.getName() + ", 스레드 ID: " + thread.getId());
                Log.e("다눔 애플리케이션", "발생한 예외: " + throwable.getMessage(), throwable);

                StringWriter stringWriter = new StringWriter();
                PrintWriter printWriter = new PrintWriter(stringWriter);
                throwable.printStackTrace(printWriter);
                Log.e("다눔 애플리케이션", "예외 스택 트레이스: \n" + stringWriter.toString());

                new Handler(Looper.getMainLooper()).post(() -> {
                    Toast.makeText(getApplicationContext(), "앱에서 예기치 않은 오류가 발생했습니다. 다시 시도해주세요.", Toast.LENGTH_LONG).show();
                });

                try {
                } catch (Exception e) {
                    Log.e("다눔 애플리케이션", "앱 종료 전 정리 작업 중 오류 발생: " + e.getMessage());
                }

                // 원래의 기본 예외 처리기를 호출하여 시스템에 예외 전달
                if (defaultHandler != null) {
                    defaultHandler.uncaughtException(thread, throwable);
                } else {
                    // 기본 예외 처리기가 없을 경우 앱 강제 종료
                    System.exit(2);
                }
            }
        });

        Log.d("다눔 애플리케이션", "전역 예외 처리기가 설정되었습니다.");
    }

    public static Context getAppContext() {
        return instance.getApplicationContext();
    }
}