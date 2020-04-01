package io.homo_efficio.ecotrip.api.admin.eco_program.service;

import io.homo_efficio.ecotrip.domain.eco_program.entity.Region;

import java.util.List;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-04-01
 */
public interface RegionService {

    List<Region> getRegionsFromRaw(String raw);
}
