package com.yeoro.domain.user.model.service;

import com.yeoro.domain.user.model.dto.UserDto;
import com.yeoro.domain.user.model.dto.request.LoginRequestDto;
import com.yeoro.domain.user.model.dto.response.LoginResponseDto;
import com.yeoro.domain.user.model.mapper.UserMapper;
import com.yeoro.global.exception.CustomException;
import com.yeoro.global.security.jwt.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.yeoro.global.exception.ErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

	@Value("${file.upload-dir}")
	private String uploadPath;

	private final UserMapper userMapper;
	private final JWTUtil jwtUtil;

	@Transactional
	public Boolean register(UserDto userDto){
		return userMapper.insertUser(userDto) > 0;
	}

	@Transactional
	public Boolean unregister(String userId) {
		return userMapper.deleteUser(userId) > 0;
	}

	@Transactional
	public LoginResponseDto login(LoginRequestDto loginRequestDto) {
		UserDto loginUser = Optional.ofNullable(userMapper.login(loginRequestDto))
				.orElseThrow(() -> new CustomException(USER_NOT_FOUND));

		String accessToken = jwtUtil.createAccessToken(loginUser.getUserId());
		String refreshToken = jwtUtil.createRefreshToken(loginUser.getUserId());

		saveRefreshToken(loginUser.getUserId(), refreshToken);

		return LoginResponseDto.builder()
				.user(loginUser)
				.accessToken(accessToken)
				.refreshToken(refreshToken)
				.build();
	}

	public Boolean updateUser(UserDto userDto, MultipartFile file) {
		try {
			if (file != null) {
				handleFileUpload(userDto, file);
			}
		} catch (Exception e) {
			log.error("파일 처리 도중 문제가 발생했습니다: {}", e.getMessage());
			throw new CustomException(FILE_PROCESSING_ERROR);
		}
		return userMapper.updateUser(userDto) > 0;
	}

	private void handleFileUpload(UserDto userDto, MultipartFile file) throws IOException {
		String saveFolder = uploadPath + File.separator + "profile";
		Files.createDirectories(Paths.get(saveFolder));

		String fileName = userDto.getUserId() + getFileExtension(file);
		Path filePath = Paths.get(saveFolder, fileName);

		// 기존 프로필 삭제
		if (Files.exists(filePath)) {
			Files.delete(filePath);
			userMapper.deletePicture(userDto.getUserId());
		}

		Files.copy(file.getInputStream(), filePath);
		userDto.setPictureUrl(fileName);
	}

	private String getFileExtension(MultipartFile file) {
		int index = Optional.ofNullable(file.getOriginalFilename())
				.map(name -> name.lastIndexOf('.'))
				.orElse(-1);
		return (index > 0 && index < file.getOriginalFilename().length() - 1)
				? "." + file.getOriginalFilename().substring(index + 1)
				: "";
	}

	public void deleteRefreshToken(String userId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", userId);
		map.put("token", null);
		userMapper.deleteRefreshToken(map);
	}

	public UserDto userInfo(String userId) {
		return Optional.ofNullable(userMapper.userInfo(userId))
				.orElseThrow(() -> new CustomException(USER_NOT_FOUND));
	}

	@Transactional
	public void saveRefreshToken(String userId, String refreshToken) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", userId);
		map.put("token", refreshToken);
		userMapper.saveRefreshToken(map);
	}

	public String refreshAccessToken(UserDto userDto) {
		String refreshToken = userDto.getRefreshToken();
		String userId = userDto.getUserId();

		// Refresh Token 유효성 검사
		if (!jwtUtil.checkToken(refreshToken)) {
			throw new CustomException(INVALID_REFRESH_TOKEN);
		}

		// 저장된 Refresh Token과 비교
		String storedRefreshToken = (String) userMapper.findRefreshTokenByUserId(userId);
		if (!refreshToken.equals(storedRefreshToken)) {
			throw new CustomException(REFRESH_TOKEN_MISMATCH);
		}

		// 새로운 Access Token 생성
		return jwtUtil.createAccessToken(userDto.getUserId());
	}


}