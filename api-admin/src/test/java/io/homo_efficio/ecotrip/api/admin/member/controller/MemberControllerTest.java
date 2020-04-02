package io.homo_efficio.ecotrip.api.admin.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.homo_efficio.ecotrip.api.admin.member.param.LoginParam;
import io.homo_efficio.ecotrip.api.admin._support.jwt.JwtAuthenticationFilter;
import io.homo_efficio.ecotrip.api.admin._support.jwt.JwtAuthorizationFilter;
import io.homo_efficio.ecotrip.api.admin._support.jwt.SecurityConstants;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

        MvcResult mvcResult = mvc.perform(post("/admin/members/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(memberJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String token = mvcResult.getResponse().getHeader("Authorization");
        assertThat(token).isNotEmpty();
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
        String token1 = mvcResult1.getResponse().getHeader("Authorization");

        MvcResult mvcResult2 = mvc.perform(post("/admin/members/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(memberJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String token2 = mvcResult2.getResponse().getHeader("Authorization");
        assertThat(token2).isNotEmpty();
    }
}
