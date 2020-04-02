package io.homo_efficio.ecotrip.api.admin.member.controller;

import io.homo_efficio.ecotrip.api.admin._support.jwt.JwtTokenGenerator;
import io.homo_efficio.ecotrip.api.admin._support.jwt.SecurityConstants;
import io.homo_efficio.ecotrip.api.admin.member.dto.LoginDto;
import io.homo_efficio.ecotrip.api.admin.member.param.LoginParam;
import io.homo_efficio.ecotrip.api.admin.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-04-02
 */
@RestController
@RequestMapping("/admin/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<LoginDto> signup(@RequestBody @Valid LoginParam param) {
        LoginDto loginDto = memberService.signup(param);
        String token = JwtTokenGenerator.generate(loginDto.getUsername());
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.put(SecurityConstants.AUTHORIZATION, List.of(SecurityConstants.BEARER_PREFIX + token));
        return new ResponseEntity<>(loginDto, headers, HttpStatus.OK);
    }

    @PostMapping("/signin")
    public ResponseEntity<LoginDto> signin(@RequestBody @Valid LoginParam param) {
        LoginDto loginDto = memberService.signin(param);
        return ResponseEntity.ok(loginDto);
    }

}
