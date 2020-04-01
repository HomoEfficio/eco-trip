package io.homo_efficio.ecotrip.api.admin.eco_program.dto;

import io.homo_efficio.ecotrip.domain.eco_program.projection.RegionAndCountPrj;
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
public class RegionAndCountsByKeyword {

    private String keyword;
    private List<RegionAndCountPrj> programs;


    public static RegionAndCountsByKeyword of(String keyword, List<RegionAndCountPrj> programs) {
        return new RegionAndCountsByKeyword(keyword, programs);
    }
}
