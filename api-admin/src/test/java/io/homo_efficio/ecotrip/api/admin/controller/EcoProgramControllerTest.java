package io.homo_efficio.ecotrip.api.admin.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.homo_efficio.ecotrip.api.admin.dto.EcoProgramDto;
import io.homo_efficio.ecotrip.api.admin.param.EcoProgramParam;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@Transactional
class EcoProgramControllerTest {

    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private WebApplicationContext ctx;

    @BeforeEach
    public void beforeEach() {
        mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

//    @DisplayName("생태 여행 프로그램 정보 파일 업로드")
//    @Test
//    public void uploadFile() throws Exception {
//        MockMultipartFile mockFile =
//                new MockMultipartFile("file", "eco-programs",
//                        MediaType.TEXT_PLAIN_VALUE, "Eco Programs\nTest Data".getBytes());
//
//        MvcResult result = mvc.perform(multipart("/admin/eco-programs/upload-programs-file").file(mockFile))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andReturn();
//
//        assertThat(result.getResponse().getContentAsString()).isEqualTo("Eco Programs");
//    }

    @DisplayName("생태 여행 프로그램 정보 추가")
    @Test
    public void createNewEcoProgramInfo() throws Exception {
        EcoProgramParam ecoProgramParam = new EcoProgramParam(null, "즐거운 코딩 여행", "힐링", 159L, "누구나 좋아하는 재귀 여행", "100번째 피보나치 수를 재귀로 구하는 방법을 알아본다.");

        mvc.perform(
                post("/admin/eco-programs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(ecoProgramParam)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("prgm_code").exists())
                .andExpect(jsonPath("prgm_name").value("즐거운 코딩 여행"))
                .andExpect(jsonPath("theme").value("힐링"))
                .andExpect(jsonPath("region_code").value(159L))
                .andExpect(jsonPath("region_name").value("충청북도 제천시"))
                .andExpect(jsonPath("desc").value("누구나 좋아하는 재귀 여행"))
                .andExpect(jsonPath("detail").value("100번째 피보나치 수를 재귀로 구하는 방법을 알아본다."))
        ;
    }

    @DisplayName("생태 여행 프로그램 정보 추가 - 필수 정보인 지역이 없는 경우")
    @Test
    public void createNewEcoProgramInfoInvalidParam() throws Exception {
        EcoProgramParam ecoProgramParam = new EcoProgramParam(null, "즐거운 코딩 여행", "힐링", null, "누구나 좋아하는 재귀 여행", "100번째 피보나치 수를 재귀로 구하는 방법을 알아본다.");

        mvc.perform(
                post("/admin/eco-programs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(ecoProgramParam)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("statusCode").exists())
                .andExpect(jsonPath("errorCode").exists())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("errors").isArray())
                .andExpect(jsonPath("errors[0].field").value("regionCode"))
        ;
    }

    @DisplayName("생태 여행 프로그램 정보 수정")
    @Test
    public void updateEcoProgramInfo() throws Exception {
        EcoProgramDto ecoProgramDto = createEcoProgram(
                "즐거운 코딩 여행", "힐링", 159L, "누구나 좋아하는 재귀 여행", "100번째 피보나치 수를 재귀로 구하는 방법을 알아본다.");


        Long id = ecoProgramDto.getId();
        EcoProgramParam newEcoProgramParam = new EcoProgramParam(id, "괴로운 코딩 여행", "위로", 161L, "누구나 싫어하는 재귀 여행", "30 번째 피보나치 수를 재귀로 구하는 방법을 알아본다.");
        mvc.perform(
                patch("/admin/eco-programs/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(newEcoProgramParam)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("prgm_code").value(id))
                .andExpect(jsonPath("prgm_name").value("괴로운 코딩 여행"))
                .andExpect(jsonPath("theme").value("위로"))
                .andExpect(jsonPath("region_code").value(161L))
                .andExpect(jsonPath("region_name").value("충청북도 옥천군"))
                .andExpect(jsonPath("desc").value("누구나 싫어하는 재귀 여행"))
                .andExpect(jsonPath("detail").value("30 번째 피보나치 수를 재귀로 구하는 방법을 알아본다."))
        ;
    }

    @DisplayName("생태 여행 프로그램 정보 조회")
    @Test
    public void findEcoProgramInfo() throws Exception {
        EcoProgramDto ecoProgramDto = createEcoProgram(
                "즐거운 코딩 여행", "힐링", 159L, "누구나 좋아하는 재귀 여행", "100번째 피보나치 수를 재귀로 구하는 방법을 알아본다.");


        Long id = ecoProgramDto.getId();
        mvc.perform(
                get("/admin/eco-programs/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("prgm_code").value(id))
                .andExpect(jsonPath("prgm_name").value("즐거운 코딩 여행"))
                .andExpect(jsonPath("theme").value("힐링"))
                .andExpect(jsonPath("region_code").value(159L))
                .andExpect(jsonPath("region_name").value("충청북도 제천시"))
                .andExpect(jsonPath("desc").value("누구나 좋아하는 재귀 여행"))
                .andExpect(jsonPath("detail").value("100번째 피보나치 수를 재귀로 구하는 방법을 알아본다."))
        ;
    }

    @DisplayName("생태 여행 프로그램 정보 조회 by 지역")
    @ParameterizedTest
    @MethodSource("regions")
    public void findEcoProgramsByRegion(Long regionCode, int size) throws Exception {
        for (int i = 0; i < 5; i++) {
            EcoProgramDto ecoProgramDto = createEcoProgram(
                    "즐거운 코딩 여행" + i, "힐링", 159L + (i % 3), "누구나 좋아하는 재귀 여행", "100번째 피보나치 수를 재귀로 구하는 방법을 알아본다.");
        }


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

    @DisplayName("두 개의 지역을 포함하는 프로그램 등록 시 2건으로 등록된다.")
    @Test
    public void createdEcoProgramWithTwoRegions() throws Exception {
        MockMultipartFile mockFile =
                new MockMultipartFile("file", "eco-programs",
                        MediaType.TEXT_PLAIN_VALUE, "68,슬로시티 청산도로 떠나는 수학여행,\"아동·청소년 체험학습,\",전라남도 완도군 및 해남군 일대,서울 출발 ~ 청산도 입도(1일차) / 청산도 ~ 완도(2일차) / 완도 ~ 해남 ~ 서울(3일차), 우리나라 최초의 슬로시티 청산도에서 경험하는 느림의 미학을 통해 심신을 단련하고 색다른 남도의 생활을 경험해 보는 시간".getBytes());


        MvcResult result = mvc.perform(multipart("/admin/eco-programs/upload-programs-file").file(mockFile))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        List<EcoProgramDto> ecoPrograms = om.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});
        assertThat(ecoPrograms.size()).isEqualTo(2);
        assertThat(ecoPrograms.get(0).getName()).isEqualTo(ecoPrograms.get(1).getName());
        assertThat(ecoPrograms.get(0).getRegionCode()).isNotEqualTo(ecoPrograms.get(1).getRegionCode());
    }

    @DisplayName("공백없이 특수문자로 이어진 2개의 지역 포함 프로그램 등록 시 2건으로 등록된다.")
    @Test
    public void createdEcoProgramWithTwoRegionsWithSpecialChar() throws Exception {
        MockMultipartFile mockFile =
                new MockMultipartFile("file", "eco-programs",
                        MediaType.TEXT_PLAIN_VALUE, "70,다도해의 보물을 찾아서,\"농어촌생태체험,\",전라남도 완도군~여수시,\"[완도군 일대] 장보고기념관, 정도리 구계등, 완도수목원 / [보길도] 세연정, 낙서재, 곡수당, 동천석실 / [청산도] 슬로길, 명품상서마을 / [금오도] 비렁길\", 다도해해상국립공원의 약 150개의 유무인도서 중 완도지역의 특색있는 도서문화를 체험할 수 있는 도서탐방프로그램을 통한 역사와 우수한 자연경관을 감상하며 둘러보는 남도 도서생태체험프로그램".getBytes());


        MvcResult result = mvc.perform(multipart("/admin/eco-programs/upload-programs-file").file(mockFile))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        List<EcoProgramDto> ecoPrograms = om.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});
        assertThat(ecoPrograms.size()).isEqualTo(2);
        assertThat(ecoPrograms.get(0).getId()).isNotEqualTo(ecoPrograms.get(1).getId());
        assertThat(ecoPrograms.get(0).getName()).isEqualTo(ecoPrograms.get(1).getName());
        assertThat(ecoPrograms.get(0).getRegionCode()).isNotEqualTo(ecoPrograms.get(1).getRegionCode());
        assertThat(ecoPrograms.get(0).getRegionName()).isEqualTo("전라남도 완도군");
        assertThat(ecoPrograms.get(1).getRegionName()).isEqualTo("전라남도 여수시");
    }

    @DisplayName("여러 공백이 있지만 시도 정보만 있으면 시도 가 서비스 지역이 된다.")
    @Test
    public void createdEcoProgramWithOneSidoWithMoreInfos() throws Exception {
        MockMultipartFile mockFile =
                new MockMultipartFile("file", "eco-programs",
                        MediaType.TEXT_PLAIN_VALUE,
                        "49,계룡산국립공원과 함께하는 건강나누리 캠프,건강나누리캠프,대전광역시 계룡산국립공원 수통골지구,계룡산국립공원 수통골지구에서 진행됩니다.,\" 아토피피부염, 천식, 등 환경성질환 환아 대상 체험프로그램입니다.\"".getBytes());


        MvcResult result = mvc.perform(multipart("/admin/eco-programs/upload-programs-file").file(mockFile))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        List<EcoProgramDto> ecoPrograms = om.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});
        assertThat(ecoPrograms.size()).isEqualTo(1);
        assertThat(ecoPrograms.get(0).getRegionName()).isEqualTo("대전광역시");
    }

    @DisplayName("공백이 있지만 시도 정보만 있으면 시도 가 서비스 지역이 된다.")
    @Test
    public void createdEcoProgramWithOneSidoWithBlankComma() throws Exception {
        MockMultipartFile mockFile =
                new MockMultipartFile("file", "eco-programs",
                        MediaType.TEXT_PLAIN_VALUE,
                        "51,북한산국립공원과 함께하는 FUSION SEOUL 탐방,\"아동·청소년 체험학습,\",서울특별시 ,,\" 서울을 숨 쉬게 하는 곳, 북한산 둘레길! 그곳엔 자연생태 뿐만 아니라 순국선열의 얼이 담겨있다. 모든 곳이 놀이터와 같은 서울에서 역사도 배우고, 문화를 느끼며 친구들과 신나게 놀이기구도 탈 수 있는 기회가 바로 여기에 있다. \"".getBytes());


        MvcResult result = mvc.perform(multipart("/admin/eco-programs/upload-programs-file").file(mockFile))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        List<EcoProgramDto> ecoPrograms = om.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});
        assertThat(ecoPrograms.size()).isEqualTo(1);
        assertThat(ecoPrograms.get(0).getRegionName()).isEqualTo("서울특별시");
    }



    private static Stream<Arguments> regions() {
        return Stream.of(
                Arguments.of(159L, 2),
                Arguments.of(160L, 2),
                Arguments.of(161L, 1)
        );
    }

    private EcoProgramDto createEcoProgram(String name, String theme, Long regionCode, String desc, String detail) throws Exception {
        EcoProgramParam ecoProgramParam = new EcoProgramParam(null, name, theme, regionCode, desc, detail);

        MvcResult mvcResult = mvc.perform(
                post("/admin/eco-programs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(ecoProgramParam)))
                .andReturn();
        return om.readValue(mvcResult.getResponse().getContentAsString(), EcoProgramDto.class);
    }
}
