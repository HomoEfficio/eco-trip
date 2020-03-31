package io.homo_efficio.ecotrip.api.admin.service;

import io.homo_efficio.ecotrip.api.admin.dto.EcoProgramDto;
import io.homo_efficio.ecotrip.api.admin.param.EcoProgramParam;
import io.homo_efficio.ecotrip.domain.entity.EcoProgram;
import io.homo_efficio.ecotrip.domain.entity.Region;
import io.homo_efficio.ecotrip.domain.repository.EcoProgramRepository;
import io.homo_efficio.ecotrip.domain.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-03-30
 */
@Service
@RequiredArgsConstructor
@Transactional
public class EcoProgramServiceImpl implements EcoProgramService {

    public static final String COMMA_REPLACER = "=:=:=";
    private final RegionRepository regionRepository;
    private final EcoProgramRepository ecoProgramRepository;

    @Override
    public List<EcoProgramDto> loadEcoProgramsFromPath(Path filePath) throws IOException {
        List<EcoProgramDto> loadedEcoPrograms = new ArrayList<>();
        List<String> lines = Files.readAllLines(filePath);
        for (String line : lines) {
            List<String> cols = extractCols(line);
            String regionName = cols.get(3);
            List<Region> regions = regionRepository.findAllByNameContaining(regionName);
            if (regions.size() == 1) {
                Region region = regions.get(0);
                EcoProgram ecoProgram = ecoProgramRepository.save(new EcoProgram(null, cols.get(1), cols.get(2), region, cols.get(4), cols.get(5)));
                loadedEcoPrograms.add(EcoProgramDto.from(ecoProgram));
            }
            else
                throw new RuntimeException(String.format("지역 키워드 [%s] 로 지역을 결정할 수 없습니다.", regionName));
        }
        return loadedEcoPrograms;
    }

    private List<String> extractCols(String ecoProgramInfo) {

        List<String> cols = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(quotProcessed(ecoProgramInfo), ",");
        while (st.hasMoreTokens()) {
            cols.add(commaRecovered(st.nextToken()));
        }
        return cols;
    }

    private String quotProcessed(String ecoProgramInfo) {
        Pattern pattern = Pattern.compile("(\"[^\"]*\")");
        Matcher matcher = pattern.matcher(ecoProgramInfo);

        List<String> olds = new ArrayList<>();
        List<String> news = new ArrayList<>();
        while (matcher.find()) {
            String group = matcher.group();
            olds.add(group);
            news.add(group
                    .replace("\"", "")
                    .replace(",", COMMA_REPLACER)
                    .trim());
        }

        for (int i = 0; i < olds.size(); i++) {
            ecoProgramInfo = ecoProgramInfo.replace(olds.get(i), news.get(i));
        }
        return ecoProgramInfo;
    }

    private String commaRecovered(String s) {
        return s.replace(COMMA_REPLACER, ",");
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
}

