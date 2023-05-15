package org.springboard.board.configs;

import lombok.RequiredArgsConstructor;
import org.springboard.board.configs.interceptors.SiteConfigInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class MvcConfig implements WebMvcConfigurer {
	// 사이트 설정 유지 인터셉터
	private final SiteConfigInterceptor siteConfigInterceptor;

	@Value("${file.upload.path}")
	private String fileUploadPath;

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/")
				.setViewName("main/index");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/uploads/**")
				.addResourceLocations("file:///" + fileUploadPath);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(siteConfigInterceptor)
				.addPathPatterns("/**");
	}
}
