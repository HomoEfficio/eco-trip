package io.homo_efficio.ecotrip.api.admin.service;

import io.homo_efficio.ecotrip.domain.entity.Region;
import io.homo_efficio.ecotrip.domain.repository.RegionRepository;
import io.homo_efficio.ecotrip.global.morpheme.KomoranUtils;
import kr.co.shineware.nlp.komoran.model.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
}
