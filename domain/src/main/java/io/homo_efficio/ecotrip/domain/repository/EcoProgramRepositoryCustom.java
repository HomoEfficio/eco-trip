package io.homo_efficio.ecotrip.domain.repository;

import io.homo_efficio.ecotrip.domain.projection.RegionAndCountPrj;

import java.util.List;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-04-01
 */
public interface EcoProgramRepositoryCustom {

    List<RegionAndCountPrj> findRegionAndCountsByDescKeyword(String keyword);
}
