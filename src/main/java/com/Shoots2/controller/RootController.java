package com.Shoots2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {

    /**
     * 루트 경로 접근 시 게시판으로 리다이렉트
     */
    @GetMapping("/")
    public String redirectToPost() {
        return "redirect:/post";
    }

    /**
     * main 경로 접근 시 게시판으로 리다이렉트 (기존 코드 호환성)
     */
    @GetMapping("/main")
    public String redirectMainToPost() {
        return "redirect:/post";
    }
}