package com.Shoots2.task;

import com.Shoots2.domain.RegularUser;
import com.Shoots2.mybatis.mapper.RegularUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class RegularUserDetailsService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(RegularUserDetailsService.class);
    private final RegularUserMapper regularUserMapper;

    public RegularUserDetailsService(RegularUserMapper regularUserMapper) {
        this.regularUserMapper = regularUserMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        logger.info("로그인 시도 - ID: {}", id);

        RegularUser regularUser = regularUserMapper.selectById(id);

        if (regularUser == null) {
            logger.info("사용자를 찾을 수 없습니다 - ID: {}", id);
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + id);
        }

        logger.info("사용자 정보 로드 완료 - ID: {}, Role: {}", id, regularUser.getRole());
        return regularUser;
    }
}