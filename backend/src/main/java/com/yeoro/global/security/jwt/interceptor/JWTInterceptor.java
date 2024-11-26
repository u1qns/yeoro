package com.yeoro.global.security.jwt.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.yeoro.global.security.jwt.util.JWTUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JWTInterceptor implements HandlerInterceptor {

	private static final String AUTHORIZATION_HEADER = "Authorization";

	private final JWTUtil jwtUtil;

	@Autowired
	public JWTInterceptor(JWTUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String token = request.getHeader(AUTHORIZATION_HEADER);

		if (token != null && !token.trim().isEmpty() && jwtUtil.checkToken(token)) {
			log.info("토큰 사용 가능 : {}", token);
			return true;
		} else {
			log.warn("토큰 사용 불가능 : {}", token);
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
			return false;
		}
	}
}
