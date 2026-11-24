
package com.example.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:taskdb-test;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password="
})
@AutoConfigureMockMvc
@Transactional
class TaskControllerDeleteTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deleteTask_shouldHideTaskButPreserveHistory() throws Exception {
        String response = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "Задача для удаления",
                                    "description": "Описание задачи"
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Integer id = objectMapper.readTree(response).get("id").asInt();

        mockMvc.perform(delete("/api/tasks/{id}", id)).andExpect(status().isNoContent());

        mockMvc.perform(get("/api/tasks/{id}", id)).andExpect(status().isNotFound());

        mockMvc.perform(get("/api/tasks").param("title", "Задача для удаления"))
                .andExpect(status().isOk()).andExpect(jsonPath("$").isEmpty());

        mockMvc.perform(get("/api/tasks/{id}/history", id)).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].eventType").value("DELETED"));
    }

    @Test
    void deleteTask_shouldReturnNotFound_whenTaskDoesNotExist() throws Exception {
        mockMvc.perform(delete("/api/tasks/{id}", 999)).andExpect(status().isNotFound());
    }
}
