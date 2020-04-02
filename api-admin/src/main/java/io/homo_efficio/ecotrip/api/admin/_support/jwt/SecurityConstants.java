package io.homo_efficio.ecotrip.api.admin._support.jwt;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-04-02
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityConstants {

    public static final String SECRET = "homo.efficio";
    public static final long EXPIRATION_SECONDS = 120 * 60 * 60 * 1000;
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String SIGN_UP_URL = "/admin/members/signup";
    public static final String SIGN_IN_URL = "/admin/members/signin";

}
