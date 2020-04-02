package io.homo_efficio.ecotrip.api.admin.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.homo_efficio.ecotrip.api.admin.member.dto.LoginDto;
import io.homo_efficio.ecotrip.api.admin.member.param.LoginParam;
import io.homo_efficio.ecotrip.api.admin._support.jwt.JwtAuthenticationFilter;
import io.homo_efficio.ecotrip.api.admin._support.jwt.JwtAuthorizationFilter;
import io.homo_efficio.ecotrip.api.admin._support.jwt.SecurityConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
class MemberControllerTest {

    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    private AuthenticationManager authenticationManager;

    @BeforeEach
    public void beforeEach() {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager, om);

        jwtAuthenticationFilter.setRequiresAuthenticationRequestMatcher(
                new AntPathRequestMatcher(SecurityConstants.SIGN_IN_URL, HttpMethod.POST.name())
        );
        jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);
        jwtAuthenticationFilter.setFilterProcessesUrl(SecurityConstants.SIGN_IN_URL);
        mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .addFilters(jwtAuthenticationFilter)
                .addFilters(new JwtAuthorizationFilter(authenticationManager))
                .alwaysDo(print())
                .build();
    }


    @DisplayName("회원가입")
    @Test
    public void signup() throws Exception {
        LoginParam member = new LoginParam("tester", "asdf!@#$");
        String memberJson = om.writeValueAsString(member);

        mvc.perform(post("/admin/members/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(memberJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("username").value("tester"))
                .andExpect(jsonPath("password").isNotEmpty())
                .andExpect(jsonPath("token").isNotEmpty())
        ;

    }

    @DisplayName("로그인")
    @Test
    public void signin() throws Exception {
        LoginParam member = new LoginParam("tester", "asdf!@#$");
        String memberJson = om.writeValueAsString(member);

        MvcResult mvcResult1 = mvc.perform(post("/admin/members/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(memberJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String token1 = mvcResult1.getResponse().getHeader(SecurityConstants.AUTHORIZATION);

        MvcResult mvcResult2 = mvc.perform(post("/admin/members/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(memberJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String token2 = mvcResult2.getResponse().getHeader(SecurityConstants.AUTHORIZATION);
        assertThat(token2).isNotEmpty();
    }

    @Disabled
    @DisplayName("토큰 재발급")
    @Test
    public void refreshToken() throws Exception {
        LoginParam member = new LoginParam("tester", "asdf!@#$");
        String memberJson = om.writeValueAsString(member);

        MvcResult mvcResult1 = mvc.perform(post("/admin/members/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(memberJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("username").value("tester"))
                .andExpect(jsonPath("password").isNotEmpty())
                .andExpect(jsonPath("token").isNotEmpty())
                .andReturn()
        ;
        LoginDto loginDto1 = om.readValue(mvcResult1.getResponse().getContentAsString(), LoginDto.class);
        String token1 = loginDto1.getToken();


        MvcResult mvcResult2 = mvc.perform(get("/admin/members/refresh-token")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(SecurityConstants.AUTHORIZATION, SecurityConstants.BEARER_PREFIX + token1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("token").isNotEmpty())
                .andReturn();
        LoginDto loginDto2 = om.readValue(mvcResult2.getResponse().getContentAsString(), LoginDto.class);
        String token2 = loginDto2.getToken();

        assertThat(token2).isNotEqualTo(token1);
    }
}
