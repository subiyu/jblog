package com.poscodx.jblog.config.app;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.poscodx.jblog.security.UserDetailsServiceImpl;

@SpringBootConfiguration
@EnableWebSecurity
public class SecurityConfig implements WebMvcConfigurer {
	@Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return new WebSecurityCustomizer() {
            @Override
            public void customize(WebSecurity web) {
            	/* /favicon.ico와 /assets/** 경로로 들어오는 요청을 보안 필터링에서 무시하도록 지정 */
                web
            		.ignoring()
            		.requestMatchers(new AntPathRequestMatcher("/favicon.ico"))
                	.requestMatchers(new AntPathRequestMatcher("/assets/**"));
            }
        };
    }
	
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	http
	    	.logout()
			.logoutUrl("/user/logout")
			.logoutSuccessUrl("/")
			.and()
			
    		.formLogin()
    		.loginPage("/user/login")
    		.loginProcessingUrl("/user/auth")	// 로그인 폼의 데이터 처리 URL 설정
    		.usernameParameter("id") 			// 로그인 폼에서 아이디 필드의 파라미터 이름을 id로 설정
    		.passwordParameter("password")		// 로그인 폼에서 비밀번호 필드의 파라미터 이름을 "password"로 설정
    		.defaultSuccessUrl("/")				// 성공했을 때 메인으로
    		.failureHandler(new AuthenticationFailureHandler() {
				@Override
				public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
						AuthenticationException exception) throws IOException, ServletException {
					request.setAttribute("id", request.getParameter("id"));
					request
						.getRequestDispatcher("/user/login")
						.forward(request, response);
				}
       		})
       		.and()
       		
       		.csrf()
       		.disable();
       	
    	return http.build();
    }
	
	
	// Authentication Manager
    @Bean
    public AuthenticationManager autenAuthenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
    	DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    	authenticationProvider.setPasswordEncoder(passwordEncoder);
    	authenticationProvider.setUserDetailsService(userDetailsService);
    	return new ProviderManager(authenticationProvider);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder(4);
    }

    @Bean
    public UserDetailsService userDetailsService() {
    	return new UserDetailsServiceImpl();
    }
}
