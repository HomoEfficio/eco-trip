package io.homo_efficio.ecotrip.api.admin.service;

import io.homo_efficio.ecotrip.domain.entity.EcoProgram;

import java.nio.file.Path;
import java.util.List;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-03-30
 */
public interface EcoProgramService {

    List<EcoProgram> loadEcoProgramsFromPath(Path filePath);
}
