package org.johnhargestam.springblackboxtesting.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class BlackBoxTest {

  @Autowired
  MockMvc mockMvc;
  @Test
  void testGet() throws Exception {
    mockMvc.perform(get("/main/resource"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.property").value("hello"));
  }
}