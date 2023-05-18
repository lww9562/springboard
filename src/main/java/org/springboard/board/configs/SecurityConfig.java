package org.springboard.board.configs;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springboard.board.models.member.LoginFailureHandler;
import org.springboard.board.models.member.LoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

@Configuration
public class SecurityConfig {
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.formLogin()
				.loginPage("/member/login")
				.usernameParameter("userId")
				.passwordParameter("userPw")
				.successHandler(new LoginSuccessHandler())
				.failureHandler(new LoginFailureHandler())
				.and()
				.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
				.logoutSuccessUrl("/member/login");

		http.authorizeHttpRequests()
				.requestMatchers("/mypage/**").authenticated()			// 회원 전용 URL
				//.requestMatchers("/admin/**").hasAuthority("ADMIN")	// 관리자 전용 URL
				.anyRequest().permitAll();		// 그 외 모든 페이지는 모든 회원이 접근 가능

		http.exceptionHandling()
						.authenticationEntryPoint((req, res, e) -> {
							String URI = req.getRequestURI();

							if(URI.indexOf("/admin") != -1) {
								res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "NOT AUTHORIZED");
							} else {
								String redirectURL = req.getContextPath() + "/member/login";
								res.sendRedirect(redirectURL);
							}
						});

		http.headers().frameOptions().sameOrigin();

		return http.build();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return w -> w.ignoring().requestMatchers("/css/**", "/js/**", "/images/**", "/errors/**");
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
