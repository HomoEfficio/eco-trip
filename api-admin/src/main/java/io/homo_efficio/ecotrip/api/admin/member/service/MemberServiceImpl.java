package io.homo_efficio.ecotrip.api.admin.member.service;

import io.homo_efficio.ecotrip.api.admin._support.jwt.JwtTokenGenerator;
import io.homo_efficio.ecotrip.api.admin.member.dto.LoginDto;
import io.homo_efficio.ecotrip.api.admin.member.param.LoginParam;
import io.homo_efficio.ecotrip.domain.member.entity.Member;
import io.homo_efficio.ecotrip.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-04-02
 */
@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;


    @Override
    public LoginDto signup(LoginParam param) {
        Member member = memberRepository.save(new Member(null, param.getUsername(),
                passwordEncoder.encode(param.getPassword())));
        String token = JwtTokenGenerator.generate(member.getUsername());
        return LoginDto.of(member, token);
    }

    @Override
    public LoginDto signin(LoginParam param) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(param.getUsername(), param.getPassword()));
        } catch (BadCredentialsException e) {
            throw new RuntimeException("로그인 정보가 올바르지 않습니다.", e);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(param.getUsername());

        return LoginDto.of(userDetails.getUsername(), userDetails.getPassword(), null);
    }
}
