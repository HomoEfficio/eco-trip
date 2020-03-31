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

    @DisplayName("여러 행으로 구성된 하나의 프로그램도 한 개로 등록된다.")
    @Test
    public void createdEcoProgramWithMultilines() throws Exception {
        MockMultipartFile mockFile =
                new MockMultipartFile("file", "eco-programs",
                        MediaType.TEXT_PLAIN_VALUE,
                        (
                                "89,내장산과 함께하는 즐거운 여행(숙박형),자연생태체험,전라북도 정읍시 내장동 59-10 내장산국립공원,내장산 자연 속 트레킹과 함께 미션체험도 즐기시고 100년 전 초가마을에서 하룻밤을 보낼 수  있는 숙박형 프로그램입니다.,\" - 내장산 탐방안내소 관람\n" +
                                " - 자연놀이 및 나뭇잎티셔츠 만들기 체험\n" +
                                " - 숲 트레킹, 미션수행 \n" +
                                " - 초가집에서 보내는 밤\n" +
                                "   (여치집만들기 체험 등)\n" +
                                " - 강정 만들기 체험\"\n"
                        ).getBytes());


        MvcResult result = mvc.perform(multipart("/admin/eco-programs/upload-programs-file").file(mockFile))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        List<EcoProgramDto> ecoPrograms = om.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});
        for (EcoProgramDto ecoProgram : ecoPrograms) {
            System.out.println(ecoProgram);
        }
        assertThat(ecoPrograms.size()).isEqualTo(1);
        assertThat(ecoPrograms.get(0).getRegionName()).isEqualTo("전라북도 정읍시");
    }

    @DisplayName("여러 행으로 구성된 여러 지역 프로그램은 지역 갯수만큼 등록된다.")
    @Test
    public void createdEcoProgramWithMultilinesAndMultiRegions() throws Exception {
        MockMultipartFile mockFile =
                new MockMultipartFile("file", "eco-programs",
                        MediaType.TEXT_PLAIN_VALUE,
                        (
                                "87,[수학여행]길에서 놀며 배우는 자연학교여행,\"아동·청소년 체험학습,\",\"전라북도 무주군, 전주시, 부안군\",덕유산국립공원 숲속 탐방-금강 레프팅-마이산 역암동굴 탐방-변산반도국립공원 갯벌 탐방,\" 백두대간의 등줄기 덕유산에서 뻗어져 내려온 산줄기와 강줄기는 마이산에서 만나 산태극 수태극을 불러왔습니다. 1억 년 전의 호수였던 신비로운 마이산에서 돌탑과   역고드름 이야기는 직접 보아야만 느낄 수 있는 신비함을 간직한 자연생태체험입니다.   \n" +
                                        " 전주는 조선왕조 500년의 발상지이며 음식문화가 발달하고 전통이 살아 숨 쉬는   온고지신의 전통도시여행입니다. 이와 더불어 서해안 갯벌여행은 해양 생태계의 생   성지이고 바닷물과 흙을 빚어 새로운 자원탄생의 소중함을 느끼게 해주는 탐험여행   입니다. \"\n"
                        ).getBytes());


        MvcResult result = mvc.perform(multipart("/admin/eco-programs/upload-programs-file").file(mockFile))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        List<EcoProgramDto> ecoPrograms = om.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});
        for (EcoProgramDto ecoProgram : ecoPrograms) {
            System.out.println(ecoProgram);
        }

        assertThat(ecoPrograms.size()).isEqualTo(3);
        assertThat(ecoPrograms.get(0).getName()).isEqualTo(ecoPrograms.get(1).getName());
        assertThat(ecoPrograms.get(1).getName()).isEqualTo(ecoPrograms.get(2).getName());
        assertThat(ecoPrograms.get(0).getRegionName()).isEqualTo("전라북도 무주군");
        assertThat(ecoPrograms.get(1).getRegionName()).isEqualTo("전라북도 전주시");
        assertThat(ecoPrograms.get(2).getRegionName()).isEqualTo("전라북도 부안군");
    }

    @DisplayName("여러 행으로 구성된 하나의 프로그램과 여러 행으로 구성되고 여러 지역에 걸친 프로그램 복합 케이스")
    @Test
    public void createdEcoProgramsWithMultilinesAndRegions() throws Exception {
        MockMultipartFile mockFile =
                new MockMultipartFile("file", "eco-programs",
                        MediaType.TEXT_PLAIN_VALUE,
                        (
                                "87,[수학여행]길에서 놀며 배우는 자연학교여행,\"아동·청소년 체험학습,\",\"전라북도 무주군, 전주시, 부안군\",덕유산국립공원 숲속 탐방-금강 레프팅-마이산 역암동굴 탐방-변산반도국립공원 갯벌 탐방,\" 백두대간의 등줄기 덕유산에서 뻗어져 내려온 산줄기와 강줄기는 마이산에서 만나 산태극 수태극을 불러왔습니다. 1억 년 전의 호수였던 신비로운 마이산에서 돌탑과   역고드름 이야기는 직접 보아야만 느낄 수 있는 신비함을 간직한 자연생태체험입니다.   \n" +
                                        " 전주는 조선왕조 500년의 발상지이며 음식문화가 발달하고 전통이 살아 숨 쉬는   온고지신의 전통도시여행입니다. 이와 더불어 서해안 갯벌여행은 해양 생태계의 생   성지이고 바닷물과 흙을 빚어 새로운 자원탄생의 소중함을 느끼게 해주는 탐험여행   입니다. \"\n" +
                                        "88,산과 바다가 함께하는 아름다운 동행,\"문화생태체험,농어촌생태체험,\",전라북도 부안,변산반도 국립공원의 아름다운 산과 바다 그리고 지역주민의 생활문화 체험 프로그램,\" 산과 바다가 아름다운 변산반도국립공원에서 숲체험, 바다체험과 더불어 바다와 들을 주 생활지로 살아가는 지역주민들의 생활문화 등 다양한 체험위주의 프로그램\"\n" +
                                        "89,내장산과 함께하는 즐거운 여행(숙박형),자연생태체험,전라북도 정읍시 내장동 59-10 내장산국립공원,내장산 자연 속 트레킹과 함께 미션체험도 즐기시고 100년 전 초가마을에서 하룻밤을 보낼 수  있는 숙박형 프로그램입니다.,\" - 내장산 탐방안내소 관람\n" +
                                        " - 자연놀이 및 나뭇잎티셔츠 만들기 체험\n" +
                                        " - 숲 트레킹, 미션수행 \n" +
                                        " - 초가집에서 보내는 밤\n" +
                                        "   (여치집만들기 체험 등)\n" +
                                        " - 강정 만들기 체험\"\n" +
                                        "90,내장산과 함께하는 즐거운 여행(당일형),건강나누리캠프,전라북도 정읍시 내장동 59-10 내장산국립공원,내장산의 자연을 느낄 수 있는 체험관광, 내장산국립공원의 자연을 느낄 수 있는 매우 유익한 체험관광 프로그램입니다."
                        ).getBytes());


        MvcResult result = mvc.perform(multipart("/admin/eco-programs/upload-programs-file").file(mockFile))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        List<EcoProgramDto> ecoPrograms = om.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});
        for (EcoProgramDto ecoProgram : ecoPrograms) {
            System.out.println(ecoProgram);
        }
        // 87은 무주, 전주, 부안 3개로 등록되므로 총 6
        assertThat(ecoPrograms.size()).isEqualTo(6);
        assertThat(ecoPrograms.get(0).getName()).isEqualTo(ecoPrograms.get(1).getName());
        assertThat(ecoPrograms.get(1).getName()).isEqualTo(ecoPrograms.get(2).getName());
        assertThat(ecoPrograms.get(0).getRegionName()).isEqualTo("전라북도 무주군");
        assertThat(ecoPrograms.get(1).getRegionName()).isEqualTo("전라북도 전주시");
        assertThat(ecoPrograms.get(2).getRegionName()).isEqualTo("전라북도 부안군");
        assertThat(ecoPrograms.get(3).getRegionName()).isEqualTo("전라북도 부안군");
        assertThat(ecoPrograms.get(4).getRegionName()).isEqualTo("전라북도 정읍시");
        assertThat(ecoPrograms.get(5).getRegionName()).isEqualTo("전라북도 정읍시");
    }

    @DisplayName("데이터 파일 등록")
    @Test
    public void loadFileDate() throws Exception {
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
            System.out.println(ecoProgram);
        }
        System.out.println("----------------------------");
        System.out.println("Total created: " + ecoPrograms.size());
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
