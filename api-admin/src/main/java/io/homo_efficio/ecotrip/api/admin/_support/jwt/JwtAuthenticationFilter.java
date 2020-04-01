package io.homo_efficio.ecotrip.api.admin._support.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.homo_efficio.ecotrip.api.admin.member.param.MemberParam;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-04-02
 */
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final ObjectMapper om;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            MemberParam memberParam = om.readValue(request.getInputStream(), MemberParam.class);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(memberParam.getUsername(), memberParam.getPassword(), new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException("요청에 사용자 로그인 정보가 없습니다.", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // TODO JWT 토큰 생성 및 응답 헤더에 추가
    }
}
