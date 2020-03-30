package io.homo_efficio.ecotrip.api.admin.param;

import io.homo_efficio.ecotrip.domain.entity.EcoProgram;
import io.homo_efficio.ecotrip.domain.entity.Region;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-03-30
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EcoProgramParam {

    private Long id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String theme;
    @NotNull
    private Long regionCode;
    private String description;
    private String detail;


    public void updateEntity(EcoProgram ecoProgram, Region region) {
        ecoProgram.setName(name);
        ecoProgram.setTheme(theme);
        ecoProgram.setRegion(region);
        ecoProgram.setDescription(description);
        ecoProgram.setDetail(detail);
    }
}
