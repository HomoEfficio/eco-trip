package io.homo_efficio.ecotrip.api.admin.member.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.homo_efficio.ecotrip.api.admin._support.jwt.JwtTokenGenerator;
import io.homo_efficio.ecotrip.api.admin._support.jwt.SecurityConstants;
import io.homo_efficio.ecotrip.api.admin.member.dto.LoginDto;
import io.homo_efficio.ecotrip.api.admin.member.param.LoginParam;
import io.homo_efficio.ecotrip.api.admin.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-04-02
 */
@RestController
@RequestMapping("/admin/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final UserDetailsService userDetailsService;

    @PostMapping("/signup")
    public ResponseEntity<LoginDto> signup(@RequestBody @Valid LoginParam param) {
        LoginDto loginDto = memberService.signup(param);

        return ResponseEntity.ok(loginDto);
    }

    @PostMapping("/signin")
    public ResponseEntity<LoginDto> signin(@RequestBody @Valid LoginParam param) {
        LoginDto loginDto = memberService.signin(param);
        return ResponseEntity.ok(loginDto);
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<LoginDto> refreshToken(HttpServletRequest request) {
        String oldToken = request.getHeader(SecurityConstants.AUTHORIZATION);
        String username = JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET))
                .build()
                .verify(oldToken.substring(SecurityConstants.BEARER_PREFIX.length()))
                .getSubject();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String newToken = JwtTokenGenerator.generate(username);
        return ResponseEntity.ok(LoginDto.of(username, null, newToken));
    }
}
