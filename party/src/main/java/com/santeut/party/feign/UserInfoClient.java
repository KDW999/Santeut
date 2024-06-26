package com.santeut.party.feign;

import com.santeut.party.common.config.FeignConfiguration;
import com.santeut.party.feign.dto.request.GetPartyMemberInfoRequest;
import com.santeut.party.feign.dto.response.FeignResponseDto;
import com.santeut.party.feign.dto.response.GetPartyMemberInfoResponse;
import com.santeut.party.feign.dto.response.UserInfoFeignResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "userInfoClient", url = "${auth-service.url}", configuration = FeignConfiguration.class)
public interface UserInfoClient {

  // 로그인 유저 정보 조회
  @GetMapping("/user/login/info")
  FeignResponseDto<UserInfoFeignResponseDto> getLoginUserInfo(
      @RequestHeader("userLoginId") int userId
  );

  // 유저 정보 조회
  @GetMapping("/user/{userId}")
  FeignResponseDto<UserInfoFeignResponseDto> getUserInfo(@PathVariable("userId") int userId);

  // 소모임 유저 정보 조회
  @PostMapping("/user/party/profile")
  ResponseEntity<FeignResponseDto<GetPartyMemberInfoResponse>> getPartyMemberInfo(
      @RequestHeader("userId") int userId,
      @RequestBody GetPartyMemberInfoRequest request
  );
}

