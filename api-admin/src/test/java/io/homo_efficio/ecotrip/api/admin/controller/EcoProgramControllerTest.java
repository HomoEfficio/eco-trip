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
