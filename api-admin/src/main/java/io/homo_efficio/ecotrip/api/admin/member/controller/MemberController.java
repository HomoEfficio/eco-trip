package io.homo_efficio.ecotrip.api.admin.member.controller;

import io.homo_efficio.ecotrip.api.admin.member.dto.LoginDto;
import io.homo_efficio.ecotrip.api.admin.member.param.LoginParam;
import io.homo_efficio.ecotrip.api.admin.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/signup")
    public ResponseEntity<LoginDto> signup(@RequestBody @Valid LoginParam param) {
        return ResponseEntity.ok(memberService.signup(param));
    }
}
