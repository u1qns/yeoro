package com.yeoro.domain.user.entity;

import com.yeoro.entityDefault.BaseTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseTime {
    private String id;
    private String password;
    private String nickname;
    private String pictureUrl;
    private String providerType;
    private String refreshToken;
}
