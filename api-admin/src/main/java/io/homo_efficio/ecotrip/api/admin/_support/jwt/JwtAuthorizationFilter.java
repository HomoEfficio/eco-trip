package io.homo_efficio.ecotrip.api.admin._support.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

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
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(SecurityConstants.AUTHORIZATION);

        if (header == null || !header.startsWith(SecurityConstants.BEARER_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(request, header);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request, String token) {
        if (!StringUtils.isEmpty(token)) {
            try {
                String member = JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET))
                        .build()
                        .verify(token.substring(SecurityConstants.BEARER_PREFIX.length()))
                        .getSubject();
                if (!StringUtils.isEmpty(member)) {
                    return new UsernamePasswordAuthenticationToken(member, null, new ArrayList<>());
                }
            } catch (TokenExpiredException e) {
                throw new RuntimeException("토큰이 만료됐습니다.", e);
            } catch (Exception e) {
                throw new RuntimeException("토큰이 올바르지 않습니다.", e);
            }

            return null;
        }
        return null;
    }
}
