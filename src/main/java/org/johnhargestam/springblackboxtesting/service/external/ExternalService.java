package org.johnhargestam.springblackboxtesting.service.external;

import org.johnhargestam.springblackboxtesting.service.external.configuration.ResourceNotFoundErrorHandler;
import org.johnhargestam.springblackboxtesting.service.external.configuration.UnexpectedJsonRootMessageConverter;
import org.johnhargestam.springblackboxtesting.service.external.response.ExternalResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

@Service
public class ExternalService {

  private final String resourceHost;
  private final RestTemplate restTemplate;
  private final AuthService authService;

  public ExternalService(
      @Value("${external.resource.host}") String resourceHost,
      AuthService authService,
      RestTemplate restTemplate
  ) {
    this.resourceHost = resourceHost;
    this.authService = authService;
    this.restTemplate = restTemplate;

    restTemplate.setErrorHandler(new ResourceNotFoundErrorHandler());
    restTemplate.getMessageConverters().add(0, new UnexpectedJsonRootMessageConverter());
  }

  public List<ExternalResource> getResources() {
    String authToken = authService.authorize();
    ExternalResource[] resources = restTemplate.getForObject(resourceHost + "?token={token}", ExternalResource[].class, authToken);
    return Optional.ofNullable(resources)
        .map(Arrays::asList)
        .orElse(emptyList());
  }
}
