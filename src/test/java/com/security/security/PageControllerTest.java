package com.security.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = SecurityApplication.class)
@WebAppConfiguration
public class PageControllerTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilter(springSecurityFilterChain)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "STUDENT", password = "123", roles = "STUDENT")
    void successWhenLoggedAsStudent() throws Exception {
        mockMvc.perform(get("/page-api/page"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void redirectWhenUnauthorized() throws Exception {
        mockMvc.perform(get("/page-api/page"))
                .andDo(print())
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "ADMIN", password = "123", roles = "ADMIN")
    void failWhenLoggedAsAdmin() throws Exception {
        mockMvc.perform(get("/page-api/page"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}
