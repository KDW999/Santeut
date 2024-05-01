package com.santeut.common.feign.service;

import com.santeut.common.common.exception.FeignClientException;
import com.santeut.common.dto.request.UserInfoFeignRequestDto;
import com.santeut.common.feign.UserInfoClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServerService {
    private final UserInfoClient userInfoClient;

    public String getNickname(Integer userId) {
        return userInfoClient.getUserInfo(userId).orElseThrow(() -> new FeignClientException("닉네임 정보를 불러오는데 실패했습니다.")).getUserNickname();
    }

    public Integer getUserId() {
        return userInfoClient.getUserInfo().orElseThrow(() -> new FeignClientException("유저 아이디 정보를 불러오는데 실패했습니다.")).getUserId();
    }
}
