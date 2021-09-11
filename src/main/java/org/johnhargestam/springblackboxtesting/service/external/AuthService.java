package org.johnhargestam.springblackboxtesting.service.external;

import org.johnhargestam.springblackboxtesting.service.external.response.Authorization;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class AuthService {

  private final String authHost;
  private final RestTemplate restTemplate;

  public AuthService(
      @Value("${external.auth.host}") String host,
      RestTemplate restTemplate
  ) {
    this.authHost = host;
    this.restTemplate = restTemplate;
  }

  public String authorize() {
    Authorization authorization = getAuthorization();
    return authorization.token();
  }

  private Authorization getAuthorization() {
    Authorization authorization = restTemplate.getForObject(authHost, Authorization.class);
    return Optional.ofNullable(authorization).orElseThrow();
  }
}
