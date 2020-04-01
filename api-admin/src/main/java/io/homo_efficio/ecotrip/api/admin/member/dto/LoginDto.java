package io.homo_efficio.ecotrip.api.admin.member.dto;

import io.homo_efficio.ecotrip.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-04-02
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginDto {

    private String username;
    private String password;


    public static LoginDto from(Member member) {
        return new LoginDto(member.getUsername(), member.getPassword());
    }
}
