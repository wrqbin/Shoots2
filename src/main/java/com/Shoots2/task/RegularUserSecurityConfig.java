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
                .csrf(csrf -> csrf.disable())  // CSRF 보호 비활성화
                .formLogin(fo -> fo
                        .loginPage("/login")
                        .loginProcessingUrl("/loginProcess")
                        .usernameParameter("id")
                        .passwordParameter("password")
                        .successHandler(new LoginSuccessHandler())
                        .failureHandler(new LoginFailHandler())
                        .permitAll()
                )
                .logout(lo -> lo
                        .logoutSuccessUrl("/post")  // 로그아웃 후 게시판으로 이동
                        .logoutUrl("/logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .authorizeHttpRequests(au -> au
                        // 정적 리소스 허용
                        .requestMatchers("/css/**", "/js/**", "/img/**", "/upload/**").permitAll()
                        // 기본 페이지 허용 - 메인 페이지를 게시판으로 변경
                        .requestMatchers("/", "/post", "/post/", "/login", "/join").permitAll()
                        .requestMatchers("/loginProcess", "/regularJoinProcess").permitAll()
                        .requestMatchers("/idcheck", "/emailcheck").permitAll()
                        // 에러 페이지 허용
                        .requestMatchers("/error/**").permitAll()
                        // 게시판 조회는 모두 허용
                        .requestMatchers("/post/list", "/post/list_ajax", "/post/detail", "/post/down").permitAll()
                        // 게시판 작성/수정/삭제는 인증 필요
                        .requestMatchers("/post/write", "/post/add", "/post/modify**", "/post/delete").authenticated()
                        .requestMatchers("/post/setAvailable", "/post/setCompleted").authenticated()
                        // 댓글 기능은 인증 필요
                        .requestMatchers("/comment/**").authenticated()
                        // 나머지 요청은 인증 필요
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler(new CustomAccessDeniedHandler())
                );

        return http.build();
    }
}