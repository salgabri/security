package com.security.security;

import com.google.gson.Gson;
import com.security.security.student.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = SecurityApplication.class)
@WebAppConfiguration
public class StudentManagementControllerTest {

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
    void failWhenStudentRoleGetStudents() throws Exception {
        mockMvc.perform(get("/api/v1/management/students")
                .with(httpBasic("STUDENT", "123")))
                .andExpect(status().isForbidden());
    }

    @Test
    void successWhenAdminRoleGetStudents() throws Exception {
        mockMvc.perform(get("/api/v1/management/students")
                .with(httpBasic("ADMIN", "123")))
                .andExpect(status().isOk());
    }

    @Test
    void failureWhenAdminTraineeRolePostStudent() throws Exception {
        Student student = new Student(1, "aaa");
        mockMvc.perform(post("/api/v1/management/students", student)
                .with(httpBasic("ADMINTRAINEE", "123")))
                .andExpect(status().isForbidden());
    }

    @Test
    void successWhenAdminRolePostStudent() throws Exception {
        Student student = new Student(1, "aaa");

        Gson gson = new Gson();
        String json = gson.toJson(student);

        mockMvc.perform(post("/api/v1/management/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(httpBasic("ADMIN", "123")))
                .andExpect(status().isOk());
    }
}
