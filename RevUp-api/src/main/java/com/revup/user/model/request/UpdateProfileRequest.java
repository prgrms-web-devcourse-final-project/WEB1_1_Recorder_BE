package com.revup.user.model.request;

import com.revup.user.entity.Profile;
import jakarta.validation.constraints.NotEmpty;

import static com.revup.user.constant.UserConstants.BASIC_IMAGE_URL;

public record UpdateProfileRequest(
        @NotEmpty(message = "닉네임은 필수 입력값입니다.")
        String nickname,
        String profileImage,

        String introduction,

        boolean includeData
) {

        public Profile toProfile() {
                String url = profileImage == null ? BASIC_IMAGE_URL : profileImage;
                return Profile.builder()
                        .nickname(nickname)
                        .profileImage(url)
                        .introduction(introduction)
                        .build();
        }
}
