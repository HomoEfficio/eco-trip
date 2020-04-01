package io.homo_efficio.ecotrip.api.admin.member.service;

import io.homo_efficio.ecotrip.api.admin.member.dto.LoginDto;
import io.homo_efficio.ecotrip.api.admin.member.param.LoginParam;
import io.homo_efficio.ecotrip.domain.member.entity.Member;
import io.homo_efficio.ecotrip.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
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


    @Override
    public LoginDto signup(LoginParam param) {
        Member member = memberRepository.save(new Member(null, param.getUsername(), param.getPassword()));
        return LoginDto.from(member);
    }
}
