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
                .csrf(csrf -> csrf.disable())  // CSRF 보호 비활성화 (필요시)
                .formLogin(fo -> fo
                        .loginPage("/login")
                        .loginProcessingUrl("/loginProcess")
                        .usernameParameter("id")
                        .passwordParameter("password")
                        .successHandler(new LoginSuccessHandler())  // 커스텀 성공 핸들러 사용
                        .failureHandler(new LoginFailHandler())     // 커스텀 실패 핸들러 사용
                        .permitAll()  // 로그인 페이지는 모두 접근 가능
                )
                .logout(lo -> lo
                        .logoutSuccessUrl("/login")
                        .logoutUrl("/logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")  // JSESSIONID 오타 수정
                        .permitAll()  // 로그아웃은 모두 접근 가능
                )
                .authorizeHttpRequests(au -> au
                        // 정적 리소스 접근 허용
                        .requestMatchers("/css/**", "/js/**", "/img/**").permitAll()
                        // 특정 URL 접근 허용
                        .requestMatchers("/login", "/join", "/main", "/").permitAll()
                        .requestMatchers("/loginProcess", "/regularJoinProcess", "/businessJoinProcess").permitAll()
                        .requestMatchers("/findRegularId", "/findBusinessId", "/findRegularPassword", "/findBusinessPassword").permitAll()
                        // 에러 페이지 접근 허용
                        .requestMatchers("/error/**").permitAll()
                        // 게시판 관련 권한 설정
                        .requestMatchers("/post/write", "/post/add", "/post/modify**", "/post/delete").authenticated()
                        .requestMatchers("/post/list", "/post/detail").permitAll()
                        // 나머지 요청은 인증 필요
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler(new CustomAccessDeniedHandler())
                );

        return http.build();
    }

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .formLogin(fo -> fo
//                        .loginPage("/login")
//                        .loginProcessingUrl("/loginProcess")
//                        .usernameParameter("id")
//                        .passwordParameter("password")
//                        .defaultSuccessUrl("/main")
//                )
//                .logout(lo -> lo
//                        .logoutSuccessUrl("/login")
//                        .logoutUrl("/logout")
//                        .invalidateHttpSession(true)
//                        .deleteCookies("JSESSION_ID")
//                )
//                .authorizeHttpRequests(au -> au
//                        // 게시판 관련 권한 설정
//                        .requestMatchers("/post/write", "/post/add", "/post/modify**", "/post/delete").authenticated()
//                        .requestMatchers("/post/list", "/post/detail").permitAll()
//                        // 기본 페이지 접근 권한
//                        .requestMatchers("/login", "/join", "/main").permitAll()
//                        .anyRequest().authenticated()
//                );
//
//        return http.build();
//    }
}