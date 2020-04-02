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
    private String token;


    public static LoginDto of(Member member, String token) {
        return new LoginDto(member.getUsername(), member.getPassword(), token);
    }

    public static LoginDto of(String username, String password, String token) {
        return new LoginDto(username, password, token);
    }
}
