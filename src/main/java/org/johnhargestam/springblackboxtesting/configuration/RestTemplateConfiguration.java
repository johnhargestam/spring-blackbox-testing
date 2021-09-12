package org.johnhargestam.springblackboxtesting.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfiguration {
  @Bean
  public RestTemplate restTemplate() {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.setErrorHandler(new ResourceNotFoundErrorHandler());
    restTemplate.getMessageConverters().add(0, new UnexpectedJsonRootMessageConverter());
    return restTemplate;
  }
}
