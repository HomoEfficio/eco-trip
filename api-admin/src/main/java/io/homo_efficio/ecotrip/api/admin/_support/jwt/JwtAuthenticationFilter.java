package io.homo_efficio.ecotrip.api.admin._support.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.homo_efficio.ecotrip.api.admin.member.param.LoginParam;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
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
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final ObjectMapper om;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, ObjectMapper om) {
        this.authenticationManager = authenticationManager;
        this.om = om;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginParam loginParam = om.readValue(request.getInputStream(), LoginParam.class);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginParam.getUsername(), loginParam.getPassword(), new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException("요청에 사용자 로그인 정보가 없습니다.", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String token = JwtTokenGenerator.generate(((User) authResult.getPrincipal()).getUsername());
        response.addHeader(SecurityConstants.AUTHORIZATION, SecurityConstants.BEARER_PREFIX + token);
    }

}
