package com.santeut.auth.controller;

import com.santeut.auth.common.response.BasicResponse;
import com.santeut.auth.common.util.ResponseUtil;
import com.santeut.auth.dto.request.HikingRecordRequest;
import com.santeut.auth.dto.request.PartyMemberInfoRequest;
import com.santeut.auth.dto.request.UpdatePasswordRequest;
import com.santeut.auth.dto.request.UpdateProfileRequest;
import com.santeut.auth.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/login/info")
    public BasicResponse userLoginInfo(HttpServletRequest request){

        log.debug("UserId: "+ request.getHeader("userId"));
        String userId = request.getHeader("userId");
        return new BasicResponse(HttpStatus.OK.value(), userService.userLoginInfo(userId));
    }

    @GetMapping("/{userId}")
    public BasicResponse userInfo(@PathVariable int userId){

        log.debug("userId: "+ userId);
        return new BasicResponse(HttpStatus.OK.value(), userService.userInfo(userId));
    }

    @GetMapping("/mypage/profile")
    public BasicResponse getMypageProfile(@AuthenticationPrincipal UserDetails userDetails){

        log.debug("MyPage Profile 조회: "+ userDetails.getUsername());
        return new BasicResponse(HttpStatus.OK.value(), userService.getMypageProfile(userDetails.getUsername()));
    }

    @PutMapping("/password")
    public BasicResponse updatePassword(@AuthenticationPrincipal UserDetails userDetails,
                                        @RequestBody UpdatePasswordRequest request){

        log.debug("Password 수정하는 유저 ID: "+ userDetails.getUsername());
        userService.updatePassword(userDetails.getUsername(), request);
        return new BasicResponse(HttpStatus.OK.value(), "패스워드 수정 성공");
    }

    @PatchMapping("/profile")
    public BasicResponse updateProfile(@AuthenticationPrincipal UserDetails userDetails,
                                       @RequestPart(value = "request", required = false) UpdateProfileRequest request,
                                       @RequestPart(value = "userProfile", required = false) MultipartFile multipartFile){

        log.debug("Profile 수정하는 유저 ID: "+ userDetails.getUsername());
        userService.updateProfile(userDetails.getUsername(), request, multipartFile);
        return new BasicResponse(HttpStatus.OK.value(), "프로필 수정 성공");
    }

    @PostMapping("/profile/image")
    public BasicResponse updateProfileImage(@AuthenticationPrincipal UserDetails userDetails,
                                            @RequestPart(value = "userProfile", required = false) MultipartFile multipartFile){

        log.debug("Image 수정하는 유저 ID: "+ userDetails.getUsername());
        userService.updateProfileImage(userDetails.getUsername(), multipartFile);
        return new BasicResponse(HttpStatus.OK.value(), "프로필 이미지 수정 성공");
    }

    @GetMapping("/level")
    public BasicResponse getLevel(@AuthenticationPrincipal UserDetails userDetails){

        log.debug("등급 조회 ID: "+ userDetails.getUsername());
        return new BasicResponse(HttpStatus.OK.value(), userService.getLevel(userDetails.getUsername()));
    }

    @GetMapping("/record")
    public BasicResponse gerRecord(@AuthenticationPrincipal UserDetails userDetails){

        log.debug("등산 기록 조회 ID: "+ userDetails.getUsername());
        return new BasicResponse(HttpStatus.OK.value(), userService.getMountainRecord(userDetails.getUsername()));
    }

    @PatchMapping("/record")
    public ResponseEntity<?> patchRecord(@RequestBody HikingRecordRequest request){
        
        log.debug("등산 기록 갱신");
        userService.patchMountainRecord(request);
        return ResponseUtil.buildBasicResponse(HttpStatus.OK, "등산 기록 갱신 완료");
    }

    @PostMapping("/party/profile")
    private ResponseEntity<BasicResponse> getPartyMemberInfo(
        @RequestBody PartyMemberInfoRequest requestDto) {
        log.info("{} 유저 프로필 조회", Arrays.toString(requestDto.userIdList.toArray()));
        return ResponseUtil.buildBasicResponse(HttpStatus.OK, userService.getPartyMemberInfo(requestDto));
    }

}
