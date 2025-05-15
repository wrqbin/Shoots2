package com.Shoots2.task;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class RegularUserSecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .formLogin(fo -> fo
                        .loginPage("/login")
                        .loginProcessingUrl("/loginProcess")
                        .usernameParameter("id")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/main")
                )
                .logout(lo -> lo
                        .logoutSuccessUrl("/login")
                        .logoutUrl("/logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSION_ID")
                )
                .authorizeHttpRequests(au -> au
                        // 게시판 관련 권한 설정
                        .requestMatchers("/post/write", "/post/add", "/post/modify**", "/post/delete").authenticated()
                        .requestMatchers("/post/list", "/post/detail").permitAll()
                        // 기본 페이지 접근 권한
                        .requestMatchers("/login", "/join", "/main").permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}