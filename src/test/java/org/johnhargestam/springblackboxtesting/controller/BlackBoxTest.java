package org.johnhargestam.springblackboxtesting.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureMockRestServiceServer
@TestPropertySource(properties = {
    "external.auth.host=http://localhost:0/auth",
    "external.resource.host=http://localhost:0/host",
})
class BlackBoxTest {

  static MockRestServiceServer server;
  static RestTemplate restTemplate;

  @TestConfiguration
  static class TestRestTemplateConfiguration {
    @Bean
    public RestTemplate restTemplate() {
      return restTemplate;
    }
  }

  @BeforeAll
  static void beforeAll() {
    restTemplate = new RestTemplate();
    server = MockRestServiceServer.createServer(restTemplate);
    server.expect(requestTo("http://localhost:0/auth"))
        .andRespond(withSuccess("{ \"token\": \"authorized\" }", MediaType.APPLICATION_JSON));
  }

  @Autowired
  MockMvc mockMvc;

  @BeforeEach
  void beforeEach() {
    server.reset();
  }

  @AfterEach
  void afterEach() {
    server.verify();
  }

  @Test
  void testGetJsonArray() throws Exception {
    server.expect(requestTo("http://localhost:0/host?token=authorized"))
        .andRespond(withSuccess("[{ \"property\": \"value\" }]", MediaType.APPLICATION_JSON));

    mockMvc.perform(get("/main/resource"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].property").value("value"));
  }

  @Test
  void testGetEmptyJsonArray() throws Exception {
    server.expect(requestTo("http://localhost:0/host?token=authorized"))
        .andRespond(withSuccess("[]", MediaType.APPLICATION_JSON));

    mockMvc.perform(get("/main/resource"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isEmpty())
        .andReturn();
  }

  @Test
  void testGetUnexpectedJsonObject() throws Exception {
    server.expect(requestTo("http://localhost:0/host?token=authorized"))
        .andRespond(withSuccess("{}", MediaType.APPLICATION_JSON));

    mockMvc.perform(get("/main/resource"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isEmpty())
        .andReturn();
  }
}