package io.homo_efficio.ecotrip.api.admin._support.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

import java.util.Date;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-04-02
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtTokenGenerator {

    public static String generate(String subject) {
        return JWT.create()
                .withSubject(subject)
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_SECONDS))
                .sign(Algorithm.HMAC512(SecurityConstants.SECRET));
    }
}
