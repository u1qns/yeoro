package com.yeoro.domain.user.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.yeoro.domain.user.model.dto.response.LoginResponseDto;
import com.yeoro.domain.user.model.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.yeoro.domain.user.model.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
@Slf4j
@Tag(name = "회원 인증 컨트롤러", description = "로그인, 로그아웃, 토큰 처리 등 회원의 인증 관련 처리하는 클래스.")
public class UserController {

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@Operation(summary = "회원 가입", description = "회원 정보를 입력받아 user 테이블에 저장합니다.")
	@PostMapping("/register")
	public ResponseEntity<Void> register(@RequestBody UserDto userDto) throws SQLException {
		userService.register(userDto);
		return ResponseEntity.status(201).build(); // 201 Created
	}

	@Operation(summary = "회원 탈퇴", description = "회원 아이디를 받아 user 테이블에서 회원 정보를 삭제합니다.")
	@DeleteMapping("/unregister/{userId}")
	public ResponseEntity<Void> unregister(
			@PathVariable String userId,
			HttpServletRequest request) throws SQLException {
		String token = request.getHeader("Authorization");
		userService.unregister(userId, token);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "로그인", description = "아이디와 패스워드로 로그인을 시도합니다.")
	@PostMapping("/login")
	public ResponseEntity<LoginResponseDto> login(@RequestBody UserDto userDTO) throws Exception {
		LoginResponseDto loginUser = userService.login(userDTO);
		return ResponseEntity.ok(loginUser);
	}

	@Operation(summary = "내 정보 수정", description = "회원 아이디와 토큰 정보로 인증 후, 입력받은 사용자 정보로 갱신합니다.")
	@PutMapping(value = "/my", consumes = "multipart/form-data")
	public ResponseEntity<Void> modifyUser(
			@RequestPart("userDto") UserDto userDto,
			@RequestPart(value = "file", required = false) MultipartFile file) throws Exception {
		userService.updateUser(userDto, file);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "로그아웃", description = "회원 정보를 담은 Token을 제거합니다.")
	@GetMapping("/logout/{userId}")
	@Hidden
	public ResponseEntity<Void> removeToken(
			@PathVariable("userId") String userId,
			HttpServletRequest request) throws Exception {
		String token = request.getHeader("Authorization");
		userService.deleteRefreshToken(userId, token);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "내 정보 조회", description = "회원 아이디와 토큰 정보로 인증 후, 사용자 정보를 DTO로 반환합니다.")
	@GetMapping("/my/{userId}")
	public ResponseEntity<UserDto> getInfo(
			@PathVariable("userId") @Parameter(description = "인증할 회원의 아이디.", required = true) String userId,
			HttpServletRequest request) throws Exception {
		String token = request.getHeader("Authorization");
		UserDto userDto = userService.userInfo(userId, token);
		return ResponseEntity.ok(userDto);
	}

	@Operation(summary = "Access Token 재발급", description = "만료된 access token을 재발급 받습니다.")
	@PostMapping("/refresh")
	public ResponseEntity<Void> refreshToken(@RequestBody UserDto userDTO, HttpServletRequest request) throws Exception {
		String refreshToken = request.getHeader("refreshToken");
		userService.refreshAccessToken(userDTO, refreshToken);
		return ResponseEntity.ok().build();
	}
}
