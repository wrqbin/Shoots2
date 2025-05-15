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

        // 1. 인증된 사용자 정보 가져오기
        RegularUser regularUser = (RegularUser) authentication.getPrincipal();

        // 2. 세션에 사용자 정보 저장
        HttpSession session = request.getSession();

        session.setAttribute("id", regularUser.getUser_id());
        session.setAttribute("idx", regularUser.getIdx());
        session.setAttribute("role", regularUser.getRole());
        session.setAttribute("usertype", "A"); // 일반 사용자 타입 A로 설정

        // 필요한 경우 추가 세션 속성 설정
        // session.setAttribute("name", regularUser.getName());
        // session.setAttribute("email", regularUser.getEmail());

        logger.info("세션 ID = {}", regularUser.getUser_id());
        logger.info("IDX = {}", regularUser.getIdx());
        logger.info("Role = {}", regularUser.getRole());

        // 3. 로그인 성공 후 리다이렉트
        // 이전 요청 페이지가 있으면 해당 페이지로, 없으면 메인 페이지로 이동
        String targetUrl = determineTargetUrl(request, response);
        response.sendRedirect(targetUrl);
    }

    // 리다이렉트할 URL 결정
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
        // 이전에 접근하려던 페이지가 있는 경우 해당 페이지로 리다이렉트
        String targetUrl = (String) request.getSession().getAttribute("prevPage");

        if (targetUrl != null) {
            request.getSession().removeAttribute("prevPage");
            return targetUrl;
        }

        // 이전 페이지 정보가 없으면 메인 페이지로 리다이렉트
        return "/Shoots2/mainBefore";
    }
}