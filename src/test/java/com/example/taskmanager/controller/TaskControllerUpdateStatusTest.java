
package com.example.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:taskdb-test;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password="
})
@AutoConfigureMockMvc
@Transactional
class TaskControllerUpdateStatusTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void updateStatus_shouldReturnNoContent() throws Exception {
        String response = mockMvc.perform(post("/api/tasks").contentType(MediaType.APPLICATION_JSON).content("""
                                {
                                    "title": "Тестовая задача",
                                    "description": "Описание задачи"
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Integer id = objectMapper.readTree(response).get("id").asInt();

        mockMvc.perform(patch("/api/tasks/{id}/status", id).contentType(MediaType.APPLICATION_JSON).content("""
                                {
                                    "status": "IN_PROGRESS"
                                }
                                """)).andExpect(status().isNoContent());
    }

    @Test
    void updateStatus_shouldReturnNotFound_whenTaskDoesNotExist() throws Exception {
        mockMvc.perform(patch("/api/tasks/{id}/status", 999).contentType(MediaType.APPLICATION_JSON).content("""
                                {
                                    "status": "IN_PROGRESS"
                                }
                                """)).andExpect(status().isNotFound());
    }
}
