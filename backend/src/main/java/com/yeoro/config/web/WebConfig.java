package com.yeoro.config.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import com.yeoro.global.security.jwt.interceptor.JWTInterceptor;

@Configuration
@EnableWebMvc
public class WebConfig  implements WebMvcConfigurer {
	
	private JWTInterceptor jwtInterceptor;

	@Autowired
	public WebConfig (JWTInterceptor jwtInterceptor) {
		this.jwtInterceptor = jwtInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(jwtInterceptor)
				.addPathPatterns("/**") // 모든 요청에 대해 JWT 검사
				.excludePathPatterns("/user/login", "/user/register"); // 로그인과 회원 가입은 제외
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry
			.addMapping("/**")
			.allowedOrigins("*")
			.allowedMethods(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.PUT.name(),
						HttpMethod.DELETE.name(), HttpMethod.HEAD.name(), HttpMethod.OPTIONS.name(),
						HttpMethod.PATCH.name())
			.maxAge(1800); // Pre-flight Caching
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/img/**").addResourceLocations("classpath:/static/assets/img/upload/profile/");
		registry.addResourceHandler("/*.html**").addResourceLocations("classpath:/static/");
	}

}
