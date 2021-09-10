package org.johnhargestam.springblackboxtesting.service.external;

import org.johnhargestam.springblackboxtesting.service.external.response.AuthResponse;
import org.johnhargestam.springblackboxtesting.service.external.response.ExternalResourceResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class ExternalService {

  private final String authHost;
  private final String resourceHost;
  private final RestTemplate restTemplate;

  private String authToken;

  public ExternalService(
      @Value("${external.auth.host}") String authHost,
      @Value("${external.resource.host}") String resourceHost,
      RestTemplate restTemplate
  ) {
    this.authHost = authHost;
    this.resourceHost = resourceHost;
    this.restTemplate = restTemplate;
  }

  @EventListener(ApplicationReadyEvent.class)
  public void authorize() {
    AuthResponse response = restTemplate.getForObject(authHost, AuthResponse.class);
    authToken = Objects.requireNonNull(response).token();
  }

  public String getResource() {
    ExternalResourceResponse response = restTemplate.getForObject(resourceHost + "?token={token}", ExternalResourceResponse.class, authToken);
    return Objects.requireNonNull(response).property();
  }
}
