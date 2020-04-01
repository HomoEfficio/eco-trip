package io.homo_efficio.ecotrip.domain.eco_program.projection;

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
@AllArgsConstructor
public class RegionAndCountPrj {

    private String region;
    private long count;
}
