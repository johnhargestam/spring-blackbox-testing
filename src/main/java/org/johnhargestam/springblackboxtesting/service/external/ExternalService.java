package org.johnhargestam.springblackboxtesting.service.external;

import org.johnhargestam.springblackboxtesting.service.external.configuration.UnexpectedJsonRootMessageConverter;
import org.johnhargestam.springblackboxtesting.service.external.configuration.ResourceNotFoundErrorHandler;
import org.johnhargestam.springblackboxtesting.service.external.response.Authorization;
import org.johnhargestam.springblackboxtesting.service.external.response.ExternalResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

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

    restTemplate.setErrorHandler(new ResourceNotFoundErrorHandler());
    restTemplate.getMessageConverters().add(0, new UnexpectedJsonRootMessageConverter());
  }

  @EventListener(ApplicationReadyEvent.class)
  public void authorize() {
    Authorization authorization = restTemplate.getForObject(authHost, Authorization.class);
    authToken = Optional.ofNullable(authorization).orElseThrow().token();
  }

  public List<ExternalResource> getResources() {
    ExternalResource[] resources = restTemplate.getForObject(resourceHost + "?token={token}", ExternalResource[].class, authToken);
    return Optional.ofNullable(resources).map(Arrays::asList).orElse(emptyList());
  }
}
