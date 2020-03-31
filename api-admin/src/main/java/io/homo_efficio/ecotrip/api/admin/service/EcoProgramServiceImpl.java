package io.homo_efficio.ecotrip.api.admin.service;

import io.homo_efficio.ecotrip.api.admin.dto.EcoProgramDto;
import io.homo_efficio.ecotrip.api.admin.param.EcoProgramParam;
import io.homo_efficio.ecotrip.domain.entity.EcoProgram;
import io.homo_efficio.ecotrip.domain.entity.Region;
import io.homo_efficio.ecotrip.domain.repository.EcoProgramRepository;
import io.homo_efficio.ecotrip.domain.repository.RegionRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
    public static final String DOUBLE_COMMA_REPLACER = ",:+:+:+:,";
    public static final String DOUBLE_COMMA_REPLACER_COMMA_REMOVED = ":+:+:+:";
    private final RegionRepository regionRepository;
    private final EcoProgramRepository ecoProgramRepository;
    private final RegionService regionService;

    @Override
    public List<EcoProgramDto> loadEcoProgramsFromPath(Path filePath) throws IOException {
        List<EcoProgramDto> loadedEcoPrograms = new ArrayList<>();
        List<String> lines = getMergedLines(Files.readAllLines(filePath));
        for (String line : lines) {
            ParsedEcoProgram p = parseProgram(line);
            List<Region> regions = regionService.getRegionsFromRaw(p.getRegionName());
            for (Region region : regions) {
                EcoProgram ecoProgram = ecoProgramRepository.save(
                        new EcoProgram(null, p.getName(), p.getTheme(), region, p.getDescription(), p.getDetail()));
                loadedEcoPrograms.add(EcoProgramDto.from(ecoProgram));
            }
        }
        return loadedEcoPrograms;
    }

    private List<String> getMergedLines(List<String> lines) {
        Pattern pattern = Pattern.compile("^[0-9]*,");

        List<String> mergedLines = new ArrayList<>();
        StringBuilder prev = new StringBuilder();
        for (String line : lines) {
            if (!pattern.matcher(line).find()) {
                prev.append(line);
            } else {
                if (!StringUtils.isEmpty(prev.toString())) {
                    mergedLines.add(prev.toString());
                }
                prev = new StringBuilder(line);
            }
        }
        mergedLines.add(prev.toString());
        return mergedLines;
    }

    private ParsedEcoProgram parseProgram(String ecoProgramInfo) {

        List<String> cols = new ArrayList<>();

        String doubleCommaReplaced = ecoProgramInfo.replace(",,", DOUBLE_COMMA_REPLACER);
        StringTokenizer st = new StringTokenizer(quotProcessed(doubleCommaReplaced), ",");
        while (st.hasMoreTokens()) {
            cols.add(commaRecovered(st.nextToken()));
        }
        return new ParsedEcoProgram(cols.get(1), cols.get(2), cols.get(3), cols.get(4), cols.get(5));
    }

    @Getter
    @AllArgsConstructor
    static class ParsedEcoProgram {
        private String name;
        private String theme;
        private String regionName;
        private String description;
        private String detail;
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
        return s.replace(COMMA_REPLACER, ",")
                .replace(DOUBLE_COMMA_REPLACER_COMMA_REMOVED, "");
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

