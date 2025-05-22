package com.Shoots2.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.io.PrintWriter;

@Controller
public class HomeController {
    private static Logger logger = LoggerFactory.getLogger(HomeController.class);


    @GetMapping("/mainBefore")
    public String home(HttpSession session, Model model) {
        if ("pending".equals(session.getAttribute("businessAccess"))) {
            // 미승인 상태 메시지 설정
            model.addAttribute("message", "미승인 상태입니다. 관리자의 승인을 기다려주세요.");
            // 세션 무효화
            session.invalidate();
            // 로그인 페이지로 리다이렉트
            return "redirect:/login";
        }
        // 정상적인 경우 메인 페이지로 이동
        return "redirect:/main";
    }

//    @GetMapping("/mainBefore") //로그인이 성공하면 main 주소로 가기 전 로그인 유저 타입을 확인하는 경로
//    public void home(HttpSession session, HttpServletResponse response) throws IOException {
//        if ("pending".equals(session.getAttribute("businessAccess"))) {
//            response.setContentType("text/html; charset=utf-8");
//            response.setCharacterEncoding("utf-8");
//
//            PrintWriter out = response.getWriter();
//            out.println("<script type='text/javascript'>");
//            out.println("if(confirm('미승인 상태입니다. 관리자의 승인을 기다려주세요.')){");
//            out.println("location.href='/Shoots2/logout';");
//            out.println("} else { location.href='/Shoots2/logout'; }");
//            out.println("</script>");
//            out.flush();
//        }
//        response.sendRedirect("/Shoots2/main");
//    }

    @GetMapping(value = "/main")
    public String main(Model model) {
        return "home/home";
    }

    @GetMapping("/error/403")
    public String error_403() {
        return "error/403";
    }
}