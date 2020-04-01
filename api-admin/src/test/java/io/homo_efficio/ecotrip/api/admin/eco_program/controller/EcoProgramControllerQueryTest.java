package io.homo_efficio.ecotrip.api.admin.eco_program.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.homo_efficio.ecotrip.api.admin.eco_program.dto.EcoProgramDto;
import io.homo_efficio.ecotrip.api.admin.eco_program.param.KeywordParam;
import io.homo_efficio.ecotrip.api.admin.eco_program.param.RegionNameParam;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-04-01
 */
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class EcoProgramControllerQueryTest {

    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private WebApplicationContext ctx;

    @BeforeAll
    public void beforeAll() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();

        File dataFile = new ClassPathResource("data/data2.csv").getFile();
        MockMultipartFile mockFile =
                new MockMultipartFile("file", "eco-programs",
                        MediaType.TEXT_PLAIN_VALUE,
                        new FileInputStream(dataFile));


        MvcResult result = mvc.perform(multipart("/admin/eco-programs/upload-programs-file").file(mockFile))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        List<EcoProgramDto> ecoPrograms = om.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});
        for (EcoProgramDto ecoProgram : ecoPrograms) {
            System.out.println(ecoProgram.getId() + "\t" + ecoProgram.getName().substring(0, Math.min(ecoProgram.getName().length(), 7)) + "\t" + ecoProgram.getRegionName());
        }
        System.out.println("----------------------------");
        System.out.println("Total created: " + ecoPrograms.size());
    }


    @DisplayName("평창군을 입력하면 평창군의 프로그램 이름과 테마를 반환한다.")
    @Test
    public void findProgramNameAndThemesByRegionKeyword() throws Exception {
        RegionNameParam regionName = new RegionNameParam("평창군");
        mvc.perform(
                get("/admin/eco-programs/by-region-name")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(regionName)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("region").value(144))
                .andExpect(jsonPath("programs").isArray())
                .andExpect(jsonPath("programs[0].prgm_name").value("오감만족! 오대산 에코 어드벤처 투어"))
                .andExpect(jsonPath("programs[0].theme").value("아동·청소년 체험학습"))
                .andExpect(jsonPath("programs[1].prgm_name").value("오대산국립공원 힐링캠프"))
                .andExpect(jsonPath("programs[1].theme").value("숲 치유,"))
                .andExpect(jsonPath("programs[2].prgm_name").value("소금강 지역문화 체험"))
                .andExpect(jsonPath("programs[2].theme").value("자연생태,"))
                .andExpect(jsonPath("programs[3].prgm_name").value("(1박2일)자연으로 떠나는 행복여행"))
                .andExpect(jsonPath("programs[3].theme").value("문화생태체험,자연생태체험,"))
        ;
    }

    @DisplayName("키워드 세계문화유산 을 입력하면 프로그램 소개 컬럼에서 키워드가 포함된 레코드의 지역 및 지역당 프로그램 수를 출력한다.")
    @Test
    public void findProgramRegionAndCountsByDescKeyword() throws Exception {
        KeywordParam keyword = new KeywordParam("세계문화유산");
        mvc.perform(
                get("/admin/eco-programs/by-desc-keyword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(keyword)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("keyword").value("세계문화유산"))
                .andExpect(jsonPath("programs").isArray())
                .andExpect(jsonPath("programs[0].region").value("경상북도 경주시"))
                .andExpect(jsonPath("programs[0].count").value(2))
        ;
    }

    @DisplayName("키워드 장보고 을 입력하면 프로그램 소개 컬럼에서 키워드가 포함된 레코드의 지역 및 지역당 프로그램 수를 출력한다.")
    @Test
    public void findProgramRegionAndCountsByDescKeyword2() throws Exception {
        KeywordParam keyword = new KeywordParam("장보고");
        mvc.perform(
                get("/admin/eco-programs/by-desc-keyword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(keyword)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("keyword").value("장보고"))
                .andExpect(jsonPath("programs").isArray())
                .andExpect(jsonPath("programs[0].region").value("전라남도 여수시"))
                .andExpect(jsonPath("programs[0].count").value(1))
                .andExpect(jsonPath("programs[1].region").value("전라남도 완도군"))
                .andExpect(jsonPath("programs[1].count").value(1))
        ;
    }

    @ParameterizedTest(name = "키워드 {0} 을 입력하면 모든 프로그램 상세 컬럼에서 키워드의 출현 빈도수 {1} 를 출력한다.")
    @MethodSource("keywordFreq")
    public void findProgramKeywordAndCountsByDetailKeyword(String keyword, int count) throws Exception {

        mvc.perform(
                get("/admin/eco-programs/by-detail-keyword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(keyword)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("keyword").value(keyword))
                .andExpect(jsonPath("count").value(count))
        ;
    }

    private static Stream<Arguments> keywordFreq() {
        return Stream.of(
                Arguments.of("문화", 62),
                Arguments.of("서바이벌", 1),
                Arguments.of("트레킹", 5),
                Arguments.of("해변", 4),
                Arguments.of("골목길", 1)
        );
    }


    @DisplayName("생태 여행 프로그램 정보 조회 by 지역")
    @ParameterizedTest
    @MethodSource("regions")
    public void findEcoProgramsByRegion(Long regionCode, int size) throws Exception {
        MvcResult mvcResult = mvc.perform(
                get("/admin/eco-programs" + "?regionCode=" + regionCode)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        List<EcoProgramDto> ecoProgramDtos = om.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {});

        assertThat(ecoProgramDtos.size()).isEqualTo(size);
    }


    private static Stream<Arguments> regions() {
        return Stream.of(
                Arguments.of(159L, 2),
                Arguments.of(160L, 6),
                Arguments.of(161L, 0),
                Arguments.of(273L, 1)
        );
    }

}
