package com.Shoots2.task;

import com.Shoots2.domain.RegularUser;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    private static final Logger logger = LoggerFactory.getLogger(LoginSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        logger.info("로그인 성공 처리");

        // 인증된 사용자 정보 가져오기
        RegularUser regularUser = (RegularUser) authentication.getPrincipal();

        // 세션에 사용자 정보 저장
        HttpSession session = request.getSession();
        session.setAttribute("id", regularUser.getUser_id());
        session.setAttribute("idx", regularUser.getIdx());
        session.setAttribute("role", regularUser.getRole());
        session.setAttribute("usertype", "A"); // 일반 사용자 타입

        logger.info("세션 설정 완료 - ID: {}, IDX: {}, Role: {}",
                regularUser.getUser_id(), regularUser.getIdx(), regularUser.getRole());

        // 메인 페이지로 리다이렉트
        response.sendRedirect(request.getContextPath() + "/main");
    }
}