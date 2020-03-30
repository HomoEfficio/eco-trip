package io.homo_efficio.ecotrip.api.admin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.homo_efficio.ecotrip.domain.entity.EcoProgram;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-03-30
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EcoProgramDto {

    @JsonProperty("prgm_code")
    private Long id;

    @JsonProperty("prgm_name")
    private String name;

    @JsonProperty("theme")
    private String theme;

    @JsonProperty("region_code")
    private Long regionCode;

    @JsonProperty("region_name")
    private String regionName;

    @JsonProperty("desc")
    private String description;

    @JsonProperty("detail")
    private String detail;


    public static EcoProgramDto from(EcoProgram ecoProgram) {
        return new EcoProgramDto(
                ecoProgram.getId(),
                ecoProgram.getName(),
                ecoProgram.getTheme(),
                ecoProgram.getRegion().getId(),
                ecoProgram.getRegion().getName(),
                ecoProgram.getDescription(),
                ecoProgram.getDetail()
        );
    }
}
