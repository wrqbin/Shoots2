package com.Shoots2.controller;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @GetMapping(value = "/main")
    public String main(Model model) {
        return "home/home";
    }

    @GetMapping("/error/403")
    public String error_403() {
        return "error/403";
    }
}