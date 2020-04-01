package io.homo_efficio.ecotrip.api.admin.member.param;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-04-02
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginParam {

    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
}
