package com.yeoro.domain.user.model.dto.response;

import com.yeoro.domain.user.model.dto.UserDto;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDto {

    @Schema(description = "로그인한 사용자 정보")
    private UserDto user;

    @Schema(description = "액세스 토큰")
    private String accessToken;

    @Schema(description = "리프레시 토큰")
    private String refreshToken;
}
