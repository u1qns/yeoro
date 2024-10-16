package com.yeoro.domain.user.model.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.io.File;

import com.yeoro.domain.user.model.dto.response.LoginResponseDto;
import com.yeoro.global.security.jwt.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeoro.domain.user.model.dto.UserDto;
import com.yeoro.domain.user.model.mapper.UserMapper;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
@Service
public class UserService {

	@Value("${file.upload-dir}")
	private String UPLOAD_PATH;
	private UserMapper userMapper;

	@Autowired
	private final JWTUtil jwtUtil;

   public UserService(UserMapper userMapper, JWTUtil jwtUtil) {
       super();
       this.userMapper = userMapper;
	   this.jwtUtil = jwtUtil;
   }

	public Boolean register(UserDto userDto)
			throws SQLException {
		return userMapper.insertUser(userDto) > 0;
	}

	public Boolean unregister(String userId)
			throws SQLException {
		return userMapper.deleteUser(userId) > 0;
	}

	public LoginResponseDto login(UserDto userDTO) throws Exception {
		UserDto loginUser = userMapper.login(userDTO);

		String accessToken = jwtUtil.createAccessToken(loginUser.getUserId());
		String refreshToken = jwtUtil.createRefreshToken(loginUser.getUserId());

		saveRefreshToken(loginUser.getUserId(), refreshToken);

		return LoginResponseDto.builder()
				.user(loginUser)
				.accessToken(accessToken)
				.refreshToken(refreshToken)
				.build();
	}

	public Boolean updateUser(UserDto userDto, MultipartFile file) throws Exception {
		try {
			if (file != null) {
				String saveFolder = UPLOAD_PATH + File.separator + "profile";
				File folder = new File(saveFolder);

				if (!folder.exists())
					folder.mkdirs();

				String fileName = userDto.getUserId();
				int index = file.getOriginalFilename().lastIndexOf('.');
				if (index > 0 && index < file.getOriginalFilename().length() - 1) {
					fileName += "." + file.getOriginalFilename().substring(index + 1);
				}

				Path filePath = Paths.get(saveFolder + "/" + fileName);

				// 기존 프로필 삭제
				if(Files.exists(filePath)) {
					Files.delete(filePath);
					userMapper.deletePicture(userDto.getUserId());
				}
				log.debug("filname : {}", fileName);
				Files.copy(file.getInputStream(), filePath);
				userDto.setPictureUrl(fileName);
			}
		} catch (Exception e) {
			log.error("파일 처리 도중 문제가 생겼습니다. : {}", e);
		}

		return userMapper.updateUser(userDto) > 0;
	}

	public void deleteRefreshToken(String userId) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", userId);
		map.put("token", null);
		userMapper.deleteRefreshToken(map);
	}

	public UserDto userInfo(String userId) throws Exception {
		return userMapper.userInfo(userId);
	}

	public void saveRefreshToken(String userId, String refreshToken) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", userId);
		map.put("token", refreshToken);
		userMapper.saveRefreshToken(map);
	}

	public String refreshAccessToken(UserDto userDTO) throws Exception {
	   String refreshToken = userDTO.getRefreshToken();
	   if (!jwtUtil.checkToken(refreshToken)) {
			return "";
			//throw new UnauthorizedException("유효하지 않은 Refresh Token입니다."); // 사용자 정의 예외
		}

		// 저장된 Refresh Token과 비교
		String storedRefreshToken = (String) userMapper.getRefreshToken(userDTO.getUserId());
		if (!refreshToken.equals(storedRefreshToken)) {
			return "";
			//throw new UnauthorizedException("Refresh Token이 일치하지 않습니다.");
		}

		// 새로운 Access Token 생성
		return jwtUtil.createAccessToken(userDTO.getUserId());
	}

}