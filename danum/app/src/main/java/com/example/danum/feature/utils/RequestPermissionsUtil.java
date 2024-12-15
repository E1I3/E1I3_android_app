package com.example.danum.feature.utils;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

public class RequestPermissionsUtil {

    private Context context;

    private static final int REQUEST_LOCATION = 1;

    /** 위치 권한 SDK 버전 29 이상 **/
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private final String[] permissionsLocationUpApi29Impl = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
    };

    /** 위치 권한 SDK 버전 29 이하 **/
    @TargetApi(Build.VERSION_CODES.P)
    private final String[] permissionsLocationDownApi29Impl = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    // 생성자
    public RequestPermissionsUtil(Context context) {
        this.context = context;
    }

    // 위치 권한 요청 메서드 예제
    public void requestLocationPermissions(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // SDK 버전 29 이상
            ActivityCompat.requestPermissions(activity, permissionsLocationUpApi29Impl, REQUEST_LOCATION);
        } else {
            // SDK 버전 29 이하
            ActivityCompat.requestPermissions(activity, permissionsLocationDownApi29Impl, REQUEST_LOCATION);
        }
    }

    // 권한 요청 결과 처리 메서드 예제
    public boolean onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length > 0) {
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        // 권한이 거부됨
                        return false;
                    }
                }
                // 모든 권한이 허용됨
                return true;
            }
        }
        return false;
    }
}
