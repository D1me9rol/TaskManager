
package com.example.taskmanager.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:taskdb-test;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password="
})
@AutoConfigureMockMvc
@Transactional
class TaskControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createTask_shouldReturnBadRequest_whenTitleIsEmpty() throws Exception {
        mockMvc.perform(post("/api/tasks").contentType(MediaType.APPLICATION_JSON).content("""
                                {
                                    "title": "",
                                    "description": "Описание задачи"
                                }
                                """)).andExpect(status().isBadRequest());
    }

    @Test
    void createTask_shouldReturnBadRequest_whenTitleIsMissing() throws Exception {
        mockMvc.perform(post("/api/tasks").contentType(MediaType.APPLICATION_JSON).content("""
                                {
                                    "description": "Описание задачи"
                                }
                                """)).andExpect(status().isBadRequest());
    }

    @Test
    void updateTask_shouldReturnBadRequest_whenTitleIsEmpty() throws Exception {
        mockMvc.perform(put("/api/tasks/{id}", 1).contentType(MediaType.APPLICATION_JSON).content("""
                                {
                                    "title": " ",
                                    "description": "Описание задачи"
                                }
                                """)).andExpect(status().isBadRequest());
    }
}
