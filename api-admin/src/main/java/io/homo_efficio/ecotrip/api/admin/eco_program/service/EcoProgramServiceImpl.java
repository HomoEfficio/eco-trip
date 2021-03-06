package io.homo_efficio.ecotrip.api.admin.eco_program.service;

import io.homo_efficio.ecotrip.api.admin.eco_program.dto.*;
import io.homo_efficio.ecotrip.api.admin.eco_program.param.EcoProgramParam;
import io.homo_efficio.ecotrip.api.admin.eco_program.param.KeywordParam;
import io.homo_efficio.ecotrip.api.admin.eco_program.param.RegionNameParam;
import io.homo_efficio.ecotrip.domain.eco_program.entity.EcoProgram;
import io.homo_efficio.ecotrip.domain.eco_program.entity.Region;
import io.homo_efficio.ecotrip.domain.eco_program.projection.RegionAndCountPrj;
import io.homo_efficio.ecotrip.domain.eco_program.repository.EcoProgramRepository;
import io.homo_efficio.ecotrip.domain.eco_program.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-03-30
 */
@Service
@RequiredArgsConstructor
@Transactional
public class EcoProgramServiceImpl implements EcoProgramService {

    private final RegionRepository regionRepository;
    private final EcoProgramRepository ecoProgramRepository;
    private final RegionService regionService;
    private final EcoProgramParser ecoProgramParser;

    @Override
    public List<EcoProgramDto> loadEcoProgramsFromPath(Path filePath) throws IOException {
        List<EcoProgramDto> loadedEcoPrograms = new ArrayList<>();
        List<String> lines = ecoProgramParser.getMergedLines(filePath);
        for (String line : lines) {
            EcoProgramParser.ParsedEcoProgram p = ecoProgramParser.parseProgram(line);
            List<Region> regions = regionService.getRegionsFromRaw(p.getRegionName());
            for (Region region : regions) {
                EcoProgram ecoProgram = ecoProgramRepository.save(
                        new EcoProgram(null, p.getName(), p.getTheme(), region, p.getDescription(), p.getDetail()));
                loadedEcoPrograms.add(EcoProgramDto.from(ecoProgram));
            }
        }
        return loadedEcoPrograms;
    }


    @Override
    public EcoProgramDto save(EcoProgramParam ecoProgramParam) {
        Long regionCode = ecoProgramParam.getRegionCode();
        Region region = regionRepository.findById(regionCode)
                .orElseThrow(() -> new RuntimeException(String.format("지역 코드 [%d] 는 존재하지 않습니다.", regionCode)));
        Long ecoProgramId = ecoProgramParam.getId();
        if (ecoProgramId == null) {
            EcoProgram ecoProgram = ecoProgramRepository.save(new EcoProgram(
                    ecoProgramId, ecoProgramParam.getName(), ecoProgramParam.getTheme(),
                    region, ecoProgramParam.getDescription(), ecoProgramParam.getDetail()));
            return EcoProgramDto.from(ecoProgram);
        } else {
            EcoProgram ecoProgram = ecoProgramRepository.findById(ecoProgramId)
                    .orElseThrow(() -> new RuntimeException(String.format("생태 여행 프로그램 코드 [%s] 는 존재하지 않습니다.", ecoProgramId)));
            ecoProgramParam.updateEntity(ecoProgram, region);
            return EcoProgramDto.from(ecoProgram);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public EcoProgramDto findById(Long id) {
        EcoProgram ecoProgram = ecoProgramRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("생태 여행 프로그램 코드 [%s] 는 존재하지 않습니다.", id)));
        return EcoProgramDto.from(ecoProgram);
    }

    @Transactional(readOnly = true)
    @Override
    public List<EcoProgramDto> findByRegion(Long regionCode) {
        return ecoProgramRepository.findAllByRegion_Id(regionCode)
                .stream()
                .map(EcoProgramDto::from)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    @Override
    public NameAndThemesByRegionDto findByRegion(RegionNameParam regionNameParam) {
        String regionName = regionNameParam.getRegion();
        Region region = regionRepository.findFirstByNameContaining(regionName);
        if (region == null) {
            throw new RuntimeException(String.format("지역 이름 [%s] 는 존재하지 않습니다.", regionName));
        }
        List<NameAndThemeDto> programs = ecoProgramRepository.findAllByRegion_Id(region.getId())
                .stream()
                .map(NameAndThemeDto::from)
                .collect(toList());
        return NameAndThemesByRegionDto.of(region.getId(), programs);
    }

    @Override
    public RegionAndCountsByKeyword findByDescKeyword(KeywordParam keywordParam) {
        String keyword = keywordParam.getKeyword();
        List<RegionAndCountPrj> regionAndCount = ecoProgramRepository.findRegionAndCountsByDescKeyword(keyword);
        return RegionAndCountsByKeyword.of(keyword, regionAndCount);
    }

    @Override
    public KeywordAndFrquencyByKeywordDto findWordCountsByDetailKeyword(KeywordParam keywordParam) {
        String keyword = keywordParam.getKeyword();
        List<String> details = ecoProgramRepository.findKeywordAndCountsByDetailKeyword(keyword);

        int count = 0;
        System.out.println("------------------------------------");
        for (String detail : details) {
            System.out.println(detail);
            count += getKeywordFrequency(detail, keyword);
        }
        System.out.println("문화" + ": " + count);
        System.out.println("------------------------------------");

        return KeywordAndFrquencyByKeywordDto.of(keyword, count);
    }

    private int getKeywordFrequency(String s, String keyword) {
        String[] split = s.split(keyword);
        int count = split.length;
        if (!s.endsWith(keyword)) count--;
        return count;
    }
}

