package io.homo_efficio.ecotrip.api.admin.service;

import io.homo_efficio.ecotrip.domain.entity.EcoProgram;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-03-30
 */
@Service
@RequiredArgsConstructor
public class EcoProgramServiceImpl implements EcoProgramService {

    @Override
    public List<EcoProgram> loadEcoProgramsFromPath(Path filePath) {
        return Collections.emptyList();
    }
}
