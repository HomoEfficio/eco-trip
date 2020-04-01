package io.homo_efficio.ecotrip.domain.eco_program.repository;

import io.homo_efficio.ecotrip.domain.eco_program.projection.RegionAndCountPrj;

import java.util.List;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-04-01
 */
public interface EcoProgramRepositoryCustom {

    List<RegionAndCountPrj> findRegionAndCountsByDescKeyword(String keyword);

    List<String> findKeywordAndCountsByDetailKeyword(String keyword);
}
