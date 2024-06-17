package com.poscodx.jblog.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.poscodx.jblog.vo.UserVo;

public class AuthInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		//1. handler 종류 확인
		if(!(handler instanceof HandlerMethod)) {
			// DefaultServletHandler가 처리하는 경우(정적자원, /assets/**, mapping이 안돼있는
			return true;
		}
		
		//2. casting
		HandlerMethod handlerMethod = (HandlerMethod)handler;
		
		//3. Handler Method의 @Auth 가져오기
		Auth auth = handlerMethod.getMethodAnnotation(Auth.class);
		
		//4. Handler Method에 @Auth가 없으면 Type(Class)에 붙어 있는지 확인
		if(auth == null) {
			auth = handlerMethod.getBeanType().getAnnotation(Auth.class);
		}
		
		//5. Type이나 Method에 @Auth가 없는 경우
		if(auth == null) {
			return true;
		}
		
		//6. @Auth가 붙어 있기 때문에 인증(Authentication) 여부 확인
		HttpSession session = request.getSession();
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		if(authUser == null) {
			response.sendRedirect(request.getContextPath() + "/user/login");
			return false;
		}
		
		//7. 권한(Authorizaion) 체크를 위해 @Auth의 id 가져오기
		String id = auth.id();
		System.out.println("aaaaaaaaaaaaaaaaaaaaaaa" + id);
		
		// 8. 현재 사용자의 id와 @Auth의 id가 일치하는지 확인
		if(id.equals(request.getParameter("id"))) {
			return true;
		}
		
		// 9. 사용자가 자신의 id가 아닌 다른 id로 접근하려는 경우
		if(!authUser.getId().equals(request.getParameter("id"))) {
			response.sendRedirect(request.getContextPath());
			return false;
		}
		
		//10. 옳은 관리자 권한 @Auth(id={#id}), authUser.getRole() == {#id}
		return true;
	}

}
