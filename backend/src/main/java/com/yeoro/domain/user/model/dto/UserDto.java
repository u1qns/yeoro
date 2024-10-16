package com.yeoro.domain.user.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    @Schema(description = "아이디")
    private String userId;
    @Schema(description = "비밀번호")
    private String password;
    @Schema(description = "닉네임")
    private String nickname;
    @Schema(description = "유저 사진")
    private String pictureUrl;
    @Schema(description = "관리자 유무")
    private boolean isAdmin;
    @Schema(description = "oauth 서비스명")
    private String providerType;
    @Schema(description = "refresh Token")
    private String refreshToken;
}
