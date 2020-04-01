package io.homo_efficio.ecotrip.api.admin.service;

import io.homo_efficio.ecotrip.domain.entity.Region;
import io.homo_efficio.ecotrip.domain.repository.RegionRepository;
import io.homo_efficio.ecotrip.global.morpheme.KomoranUtils;
import kr.co.shineware.nlp.komoran.model.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-04-01
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;

    @Override
    public List<Region> getRegionsFromRaw(String raw) {
        Set<Region> resultRegions = new LinkedHashSet<>();
        String[] splitted = raw.trim().replace(",", " ").split(" ");

        Region sidoRegion = getSidoRegion(raw, splitted);

        // 시도 만 있는 경우와 2 단어로 된 정규 케이스는 바로 반환
        List<Region> regularRegions = get2WordsRegularRegions(raw, splitted, sidoRegion);
        if (regularRegions != null) {
            return regularRegions;
        }

        // 공백 split 으로 시군구 지역 추출
        fillWithIrregularRegions(splitted, sidoRegion, resultRegions);

        // 공백 split 으로 추출된 지역이 없거나 시도 지역만 추출된 경우 형태소 분석으로 추출
        fillWithMorphemeAnalysis(raw, sidoRegion, resultRegions);

        if (resultRegions.isEmpty()) {
            throw new RuntimeException(String.format("지역 키워드 [%s] 로 지역을 결정할 수 없습니다.", raw));
        }

        // 시군구 정보가 포함돼 있으면 시도 지역 정보만 포함된 불필요한 케이스는 제거
        return getUnnecessarySidoRemovedRegions(resultRegions);
    }

    private List<Region> getUnnecessarySidoRemovedRegions(Set<Region> resultRegions) {
        boolean sggIncluded = false;
        for (Region r: resultRegions) {
            if (r.getName().split(" ").length > 1) {
                sggIncluded = true;
                break;
            }
        }
        if (resultRegions.size() > 1 && sggIncluded) {
            return resultRegions.stream().filter(r -> r.getName().split(" ").length > 1).collect(toList());
        } else {
            return new ArrayList<>(resultRegions);
        }
    }

    private void fillWithMorphemeAnalysis(String raw, Region sidoRegion, Set<Region> resultRegions) {
        if (resultRegions.isEmpty() || (resultRegions.size() == 1 && !resultRegions.iterator().next().getName().contains(" "))) {
            List<Token> morphemes = KomoranUtils.getMorphemes(raw);
            List<String> nnps = morphemes.stream()
                    .filter(m -> m.getPos().equals(KomoranUtils.POS.NNP.name()))
                    .filter(m -> m.getMorph().length() > 1)
                    .map(Token::getMorph)
                    .collect(toList());

            for (String nnp: nnps) {
                Region region = regionRepository.findFirstByNameContaining(nnp);
                if (region != null && region.getName().contains(sidoRegion.getName())) resultRegions.add(region);
            }
        }
    }

    private void fillWithIrregularRegions(String[] splitted, Region sidoRegion, Set<Region> regions) {
        List<String> words = Arrays.stream(splitted).collect(toList());
        for (String word : words) {
            Region region = regionRepository.findFirstByNameContaining(word);
            if (region != null && region.getName().contains(sidoRegion.getName())) regions.add(region);
        }
    }

    private List<Region> get2WordsRegularRegions(String raw, String[] splitted, Region sidoRegion) {
        if (splitted.length == 1) {
            return List.of(sidoRegion);
        } else if (splitted.length == 2) {
            Region region = regionRepository.findFirstByNameContaining(raw);
            if (region != null) return List.of(region);
        }
        return null;
    }

    private Region getSidoRegion(String raw, String[] splitted) {
        Region sidoRegion = null;
        if (splitted[0].endsWith("시") || splitted[0].endsWith("도")) {
            sidoRegion = regionRepository.findFirstByNameContaining(splitted[0]);
        }
        if (sidoRegion == null) throw new RuntimeException(String.format("지역 키워드 [%s] 에 정확한 시도 정보가 없습니다.", raw));
        return sidoRegion;
    }
}
