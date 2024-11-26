package com.yeoro.domain.user.model.mapper;

import java.sql.SQLException;
import java.util.Map;

import com.yeoro.domain.user.model.dto.request.LoginRequestDto;
import org.apache.ibatis.annotations.Mapper;

import com.yeoro.domain.user.model.dto.UserDto;

@Mapper
public interface UserMapper {
	int insertUser(UserDto userDto);
	int updateUser(UserDto userDto);
	int deleteUser(String userId);
	int deletePicture(String userId);

	UserDto login(LoginRequestDto loginRequestDto);
	UserDto userInfo(String userId);

	int saveRefreshToken(Map<String, String> map);
	String findRefreshTokenByUserId(String userid);
	int deleteRefreshToken(Map<String, String> map);
}
