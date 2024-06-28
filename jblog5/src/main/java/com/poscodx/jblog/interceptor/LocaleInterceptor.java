package com.poscodx.jblog.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;

public class LocaleInterceptor implements HandlerInterceptor {
	@Autowired
    private LocaleResolver localeResolver;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	String language = localeResolver.resolveLocale(request).getLanguage();
        System.out.println("resolver-locale:" + language);
        request.setAttribute("language", language);
        return true;
    }
}
