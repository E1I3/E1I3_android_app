package com.example.danum.core.data.model;

import lombok.Getter;

@Getter
public class UserProfile {
    private final String token;
    private final String nickname;
    private final String profileImage;
    private final String email;
    private final String gender;
    private final String ageRange;
    private final String birthday;
    private final String birthYear;
    private final String phoneNumber;

    private final String userId;
    private final int type;
    private final String storeId;

    private UserProfile(Builder builder) {
        this.token = builder.token;
        this.nickname = builder.nickname;
        this.profileImage = builder.profileImage;
        this.email = builder.email;
        this.gender = builder.gender;
        this.ageRange = builder.ageRange;
        this.birthday = builder.birthday;
        this.birthYear = builder.birthYear;
        this.phoneNumber = builder.phoneNumber;
        this.userId = builder.userId;
        this.type = builder.type;
        this.storeId = builder.storeId;
    }

    // 로그인 성공 시 호출할 메서드
    public static UserProfile loginSuccess(String userId, int type, String storeId) {
        return new Builder()
                .userId(userId)
                .type(type)
                .storeId(storeId)
                .build();
    }

    public static UserProfile from(String token, String nickname, String profileImage, String email,
                                   String gender, String ageRange, String birthday, String birthYear, String phoneNumber) {
        return new Builder()
                .token(token)
                .nickname(nickname)
                .profileImage(profileImage)
                .email(email)
                .gender(gender)
                .ageRange(ageRange)
                .birthday(birthday)
                .birthYear(birthYear)
                .phoneNumber(phoneNumber)
                .build();
    }

    public static class Builder {
        private String token;
        private String nickname;
        private String profileImage;
        private String email;
        private String gender;
        private String ageRange;
        private String birthday;
        private String birthYear;
        private String phoneNumber;

        private String userId;
        private int type;
        private String storeId;

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public Builder nickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public Builder profileImage(String profileImage) {
            this.profileImage = profileImage;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder gender(String gender) {
            this.gender = gender;
            return this;
        }

        public Builder ageRange(String ageRange) {
            this.ageRange = ageRange;
            return this;
        }

        public Builder birthday(String birthday) {
            this.birthday = birthday;
            return this;
        }

        public Builder birthYear(String birthYear) {
            this.birthYear = birthYear;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder type(int type) {
            this.type = type;
            return this;
        }

        public Builder storeId(String storeId) {
            this.storeId = storeId;
            return this;
        }

        public UserProfile build() {
            return new UserProfile(this);
        }
    }
}
