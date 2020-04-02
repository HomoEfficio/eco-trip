package io.homo_efficio.ecotrip.api.admin.member.service;

import io.homo_efficio.ecotrip.domain.member.entity.Member;
import io.homo_efficio.ecotrip.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-04-02
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByUsername(username)
                .map(member -> new User(member.getUsername(), member.getPassword(), Collections.emptyList()))
                .orElseThrow(() -> new UsernameNotFoundException(String.format("사용자 [%s] 는 존재하지 않습니다.", username)));
    }
}
