package io.homo_efficio.ecotrip.api.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.homo_efficio.ecotrip.api.admin.param.EcoProgramParam;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class EcoProgramControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @DisplayName("생태 여행 프로그램 정보 파일 업로드")
    @Test
    public void uploadFile() throws Exception {
        MockMultipartFile mockFile =
                new MockMultipartFile("file", "eco-programs",
                        MediaType.TEXT_PLAIN_VALUE, "Eco Programs\nTest Data".getBytes());

        MvcResult result = mvc.perform(multipart("/admin/eco-programs/upload-programs-file").file(mockFile))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo("Eco Programs");
    }

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
}
