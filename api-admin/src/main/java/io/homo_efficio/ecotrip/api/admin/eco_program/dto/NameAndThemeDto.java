package io.homo_efficio.ecotrip.api.admin.eco_program.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.homo_efficio.ecotrip.domain.eco_program.entity.EcoProgram;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-04-01
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NameAndThemeDto {

    @JsonProperty("prgm_name")
    private String name;

    @JsonProperty("theme")
    private String theme;


    public static NameAndThemeDto from(EcoProgram ecoProgram) {
        return new NameAndThemeDto(ecoProgram.getName(), ecoProgram.getTheme());
    }
}
