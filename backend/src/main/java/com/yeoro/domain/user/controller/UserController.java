package com.yeoro.domain.user.controller;

import com.yeoro.domain.user.model.dto.request.LoginRequestDto;
import com.yeoro.domain.user.model.dto.response.LoginResponseDto;
import com.yeoro.domain.user.model.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.yeoro.domain.user.model.dto.UserDto;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;

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
	public ResponseEntity<Void> register(@RequestBody @Validated UserDto userDto) {
		userService.register(userDto);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@Operation(summary = "회원 탈퇴", description = "회원 아이디를 받아 user 테이블에서 회원 정보를 삭제합니다.")
	@DeleteMapping("/unregister/{userId}")
	public ResponseEntity<Void> unregister(@PathVariable String userId) {
		userService.unregister(userId);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "로그인", description = "아이디와 패스워드로 로그인을 시도합니다.")
	@PostMapping("/login")
	public ResponseEntity<LoginResponseDto> login(@RequestBody @Validated LoginRequestDto loginRequestDto) {
		LoginResponseDto loginUser = userService.login(loginRequestDto);
		return ResponseEntity.ok(loginUser);
	}

	@Operation(summary = "내 정보 수정", description = "회원 아이디와 토큰 정보로 인증 후, 입력받은 사용자 정보로 갱신합니다.")
	@PutMapping(value = "/my", consumes = "multipart/form-data")
	public ResponseEntity<Void> modifyUser(
			@RequestPart("userDto") @Validated UserDto userDto,
			@RequestPart(value = "file", required = false) MultipartFile file) {
		userService.updateUser(userDto, file);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "로그아웃", description = "회원 정보를 담은 Token을 제거합니다.")
	@PostMapping("/logout/{userId}")
	@Hidden
	public ResponseEntity<Void> logout(@PathVariable("userId") String userId) {
		userService.deleteRefreshToken(userId);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "내 정보 조회", description = "회원 아이디와 토큰 정보로 인증 후, 사용자 정보를 DTO로 반환합니다.")
	@GetMapping("/my/{userId}")
	public ResponseEntity<UserDto> getInfo(
			@PathVariable("userId") @Parameter(description = "인증할 회원의 아이디.", required = true) String userId) {
		UserDto userDto = userService.userInfo(userId);
		return ResponseEntity.ok(userDto);
	}

	@Operation(summary = "Access Token 재발급", description = "만료된 access token을 재발급 받습니다.")
	@PostMapping("/refresh")
	public ResponseEntity<Void> refreshToken(@RequestBody @Validated UserDto userDTO) {
		userService.refreshAccessToken(userDTO);
		return ResponseEntity.ok().build();
	}
}
