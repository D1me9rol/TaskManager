
package com.example.taskmanager.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
class TaskControllerNotFoundTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void findById_shouldReturnNotFound_whenTaskDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/tasks/{id}", 999)).andExpect(status().isNotFound());
    }

    @Test
    void updateTask_shouldReturnNotFound_whenTaskDoesNotExist() throws Exception {
        mockMvc.perform(put("/api/tasks/{id}", 999).contentType(MediaType.APPLICATION_JSON).content("""
                                {
                                    "title": "Новое название",
                                    "description": "Новое описание"
                                }
                                """)).andExpect(status().isNotFound());
    }
}
