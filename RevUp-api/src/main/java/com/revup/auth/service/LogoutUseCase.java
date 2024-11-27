package com.revup.auth.service;


import com.revup.annotation.UseCase;
import com.revup.auth.adapter.RefreshTokenAdapter;
import com.revup.user.entity.User;
import com.revup.user.util.UserUtil;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class LogoutUseCase {

    private final RefreshTokenAdapter refreshTokenAdapter;
    private final UserUtil userUtil;
    /**
     * refreshToken 제거
     */
    public void execute() {
        User currentUser = userUtil.getCurrentUser();
        Long userId = currentUser.getId();
        refreshTokenAdapter.remove(userId);
    }
}