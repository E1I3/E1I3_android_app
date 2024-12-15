package com.example.danum.core.config;

import android.content.Context;

import com.kakao.sdk.user.UserApiClient;

import javax.inject.Inject;

public class KakaoAuthClient {

    @Inject
    public KakaoAuthClient() {
    }

    public void loginWithKakaoTalk(
            Context context,
            OnSuccessListener onSuccess,
            OnFailureListener onFailure
    ) {
        UserApiClient.getInstance().loginWithKakaoTalk(context, (token, error) -> {
            if (error != null) {
                onFailure.onFailure(error);
            } else if (token != null) {
                onSuccess.onSuccess(token.getAccessToken());
            }
            return null;
        });
    }

    // 콜백 인터페이스 정의
    public interface OnSuccessListener {
        void onSuccess(String accessToken);
    }

    public interface OnFailureListener {
        void onFailure(Throwable error);
    }
}
