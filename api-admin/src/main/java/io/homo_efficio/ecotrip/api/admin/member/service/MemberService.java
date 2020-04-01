package io.homo_efficio.ecotrip.api.admin.member.service;

import io.homo_efficio.ecotrip.api.admin.member.dto.LoginDto;
import io.homo_efficio.ecotrip.api.admin.member.param.LoginParam;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-04-02
 */
public interface MemberService {

    LoginDto signup(LoginParam param);
}
