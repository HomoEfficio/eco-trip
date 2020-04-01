package io.homo_efficio.ecotrip.api.admin.service;

import io.homo_efficio.ecotrip.api.admin.dto.EcoProgramDto;
import io.homo_efficio.ecotrip.api.admin.dto.NameAndThemesByRegionDto;
import io.homo_efficio.ecotrip.api.admin.param.EcoProgramParam;
import io.homo_efficio.ecotrip.api.admin.param.RegionNameParam;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-03-30
 */
public interface EcoProgramService {

    List<EcoProgramDto> loadEcoProgramsFromPath(Path filePath) throws IOException;

    EcoProgramDto save(EcoProgramParam ecoProgramParam);

    EcoProgramDto findById(Long id);

    List<EcoProgramDto> findByRegion(Long regionCode);

    NameAndThemesByRegionDto findByRegion(RegionNameParam regionNameParam);
}
