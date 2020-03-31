package io.homo_efficio.ecotrip.api.admin.service;

import io.homo_efficio.ecotrip.api.admin.dto.EcoProgramDto;
import io.homo_efficio.ecotrip.api.admin.param.EcoProgramParam;
import io.homo_efficio.ecotrip.domain.entity.EcoProgram;
import io.homo_efficio.ecotrip.domain.entity.Region;
import io.homo_efficio.ecotrip.domain.repository.EcoProgramRepository;
import io.homo_efficio.ecotrip.domain.repository.RegionRepository;
import io.homo_efficio.ecotrip.global.morpheme.KomoranUtils;
import kr.co.shineware.nlp.komoran.model.Token;
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

    @Override
    public List<EcoProgramDto> loadEcoProgramsFromPath(Path filePath) throws IOException {
        List<EcoProgramDto> loadedEcoPrograms = new ArrayList<>();
        List<String> lines = getMergedLines(Files.readAllLines(filePath));
        for (String line : lines) {
            List<String> cols = extractCols(line);
            List<Region> regions = getRegionsFromRaw(cols.get(3));
            for (Region region : regions) {
                EcoProgram ecoProgram = ecoProgramRepository.save(new EcoProgram(null, cols.get(1), cols.get(2), region, cols.get(4), cols.get(5)));
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

    private List<Region> getRegionsFromRaw(String raw) {
        List<Region> regions = new ArrayList<>();
        String sido = null;
        Region sidoRegion = null;
        String[] splitted = raw.split(" ");
        if (splitted[0].endsWith("시") || splitted[0].endsWith("도")) {
            sido = splitted[0];
            sidoRegion = regionRepository.findFirstByNameContaining(sido);
        }
        if (sidoRegion == null) throw new RuntimeException(String.format("지역 키워드 [%s] 에 정확한 시도 정보가 없습니다.", raw));
        if (splitted.length == 1) {
            Region region = regionRepository.findFirstByNameContaining(sido);
            if (region != null) return List.of(region);
        } else if (splitted.length == 2) {
            Region region = regionRepository.findFirstByNameContaining(raw);
            if (region != null) return List.of(region);
        }
        List<Token> morphemes = KomoranUtils.getMorphemes(raw);
        List<String> sggs = morphemes.stream()
                .filter(m -> m.getPos().equals(KomoranUtils.POS.NNP.name()))
                .map(Token::getMorph)
                .filter(nnp -> nnp.endsWith("시") || nnp.endsWith("군") || nnp.endsWith("구"))
                .collect(toList());

        for (String sgg: sggs) {
            Region region = regionRepository.findFirstByNameContaining(sgg);
            if (region != null && region.getName().contains(sidoRegion.getName())) regions.add(region);
        }

        if (regions.isEmpty()) {
            throw new RuntimeException(String.format("지역 키워드 [%s] 로 지역을 결정할 수 없습니다.", raw));
        }
        return regions;
    }

    private List<String> extractCols(String ecoProgramInfo) {

        List<String> cols = new ArrayList<>();

        String doubleCommaReplaced = ecoProgramInfo.replace(",,", DOUBLE_COMMA_REPLACER);
        StringTokenizer st = new StringTokenizer(quotProcessed(doubleCommaReplaced), ",");
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

