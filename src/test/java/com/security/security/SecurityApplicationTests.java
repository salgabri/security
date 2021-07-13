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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = SecurityApplication.class)
@WebAppConfiguration
class SecurityApplicationTests {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilter(springSecurityFilterChain).build();
    }

    @Test
    void testPermitAllPage() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isOk());
    }

    @Test
	void testFailWhenNotAuthenticated() throws Exception {
    	mockMvc.perform(get("/api/v1/students")).andExpect(status().isUnauthorized());
	}

	@Test
	void testSuccessWhenAuthenticatedAndStudentRole() throws Exception {
    	mockMvc.perform(get("/api/v1/students/1")
                .with(httpBasic("STUDENT", "123")))
                .andExpect(status().isOk());
	}

    @Test
    void testFailureWhenAuthenticatedAndStudentRole() throws Exception {
        mockMvc.perform(get("/api/v1/students/1")
                .with(httpBasic("ADMIN", "123")))
                .andExpect(status().isForbidden());
    }
}
