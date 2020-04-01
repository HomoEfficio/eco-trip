package io.homo_efficio.ecotrip.api.admin.service;

import io.homo_efficio.ecotrip.api.admin.dto.EcoProgramDto;
import io.homo_efficio.ecotrip.domain.entity.EcoProgram;
import io.homo_efficio.ecotrip.domain.entity.Region;
import io.homo_efficio.ecotrip.domain.repository.EcoProgramRepository;
import io.homo_efficio.ecotrip.domain.repository.RegionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;

class EcoProgramServiceImplTest {

    @Mock
    private RegionRepository regionRepository;

    @Mock
    private EcoProgramRepository ecoProgramRepository;

    @Mock
    private EcoProgramParser ecoProgramParser;

    @Mock
    private RegionService regionService;

    @InjectMocks
    private EcoProgramServiceImpl ecoProgramService;


    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.initMocks(this);
    }

    @DisplayName("시도 시군구 서비스 지역 데이터 매칭")
    @Test
    public void match_sido_sgg() throws IOException {
        String ecoProgramInfo = "1,자연과 문화를 함께 즐기는 설악산 기행,\"문화생태체험,자연생태체험,\",강원도 속초,\"설악산 탐방안내소, 신흥사, 권금성, 비룡폭포\",\" 설악산은 왜 설악산이고, 신흥사는 왜 신흥사일까요? 설악산에 대해 정확히 알고, 배우고, 느낄 수 있는 당일형 생태관광입니다.\"";
        String fileName = "eco-1";
        BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
        bw.write(ecoProgramInfo);
        bw.close();

        Region region = new Region(139L, "강원도 속초시");
        given(regionRepository.findAllByNameContaining("강원도 속초"))
                .willReturn(List.of(region));
        given(ecoProgramRepository.save(
                argThat(eco -> eco.getRegion().getName().equals("강원도 속초시") &&
                        eco.getTheme().equals("문화생태체험,자연생태체험,"))))
                .willReturn(new EcoProgram(1L, "자연과 문화를 함께 즐기는 설악산 기행",
                        "문화생태체험,자연생태체험,", region,
                        "설악산 탐방안내소, 신흥사, 권금성, 비룡폭포",
                        "설악산은 왜 설악산이고, 신흥사는 왜 신흥사일까요? 설악산에 대해 정확히 알고, 배우고, 느낄 수 있는 당일형 생태관광입니다."));
        given(ecoProgramParser.getMergedLines(Path.of(fileName)))
                .willReturn(List.of(ecoProgramInfo));
        EcoProgramParser.ParsedEcoProgram p = new EcoProgramParser.ParsedEcoProgram(
                "자연과 문화를 함께 즐기는 설악산 기행", "문화생태체험,자연생태체험,", "강원도 속초",
                "설악산 탐방안내소, 신흥사, 권금성, 비룡폭포",
                "설악산은 왜 설악산이고, 신흥사는 왜 신흥사일까요? 설악산에 대해 정확히 알고, 배우고, 느낄 수 있는 당일형 생태관광입니다."
        );
        given(ecoProgramParser.parseProgram(ecoProgramInfo))
                .willReturn(p);
        given(regionService.getRegionsFromRaw("강원도 속초"))
                .willReturn(List.of(region));

        List<EcoProgramDto> loadedEcoPrograms = ecoProgramService.loadEcoProgramsFromPath(Path.of(fileName));

        Files.delete(Path.of(fileName));

        assertThat(loadedEcoPrograms.size()).isEqualTo(1);
        EcoProgramDto ep = loadedEcoPrograms.get(0);
        assertThat(ep.getName()).isEqualTo("자연과 문화를 함께 즐기는 설악산 기행");
        assertThat(ep.getTheme()).isEqualTo("문화생태체험,자연생태체험,");
        assertThat(ep.getRegionName()).isEqualTo("강원도 속초시");
        assertThat(ep.getDescription()).isEqualTo("설악산 탐방안내소, 신흥사, 권금성, 비룡폭포");
        assertThat(ep.getDetail()).isEqualTo("설악산은 왜 설악산이고, 신흥사는 왜 신흥사일까요? 설악산에 대해 정확히 알고, 배우고, 느낄 수 있는 당일형 생태관광입니다.");
    }

}
