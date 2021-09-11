package org.johnhargestam.springblackboxtesting.service.external;

import org.johnhargestam.springblackboxtesting.Clock;
import org.johnhargestam.springblackboxtesting.service.external.response.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class AuthService {

  private final String authHost;
  private final RestTemplate restTemplate;
  private final Clock clock;

  private volatile Authorization authorization;

  @Autowired
  public AuthService(
      @Value("${external.auth.host}") String host,
      RestTemplate restTemplate,
      Clock clock
  ) {
    this.authHost = host;
    this.restTemplate = restTemplate;
    this.clock = clock;
  }

  public synchronized String getAuthorizationToken() {
    if (authorization == null || authorization.expiry() <= clock.now()) {
      authorization = authorize();
    }
    return authorization.token();
  }

  private Authorization authorize() {
    Authorization authorization = restTemplate.getForObject(authHost, Authorization.class);
    return Optional.ofNullable(authorization).orElseThrow();
  }
}
