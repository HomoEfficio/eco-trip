package io.homo_efficio.ecotrip.api.admin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-04-01
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NameAndThemesByRegionDto {

    @JsonProperty("region")
    private Long regionCode;
    private List<NameAndThemeDto> programs;


    public static NameAndThemesByRegionDto of(Long regionCode, List<NameAndThemeDto> programs) {
        return new NameAndThemesByRegionDto(regionCode, programs);
    }
}
