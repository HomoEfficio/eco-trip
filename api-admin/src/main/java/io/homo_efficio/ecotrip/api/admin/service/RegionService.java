package io.homo_efficio.ecotrip.api.admin.service;

import io.homo_efficio.ecotrip.domain.entity.Region;

import java.util.List;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-04-01
 */
public interface RegionService {

    List<Region> getRegionsFromRaw(String raw);
}
