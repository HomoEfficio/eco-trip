package io.homo_efficio.ecotrip.api.admin.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class EcoProgramControllerTest {

    @Autowired
    private MockMvc mvc;

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
}
