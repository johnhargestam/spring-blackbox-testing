package org.johnhargestam.springblackboxtesting.service.external;

import org.johnhargestam.springblackboxtesting.Clock;
import org.johnhargestam.springblackboxtesting.service.external.response.Authorization;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class AuthServiceTest {
  final String HOST = "host";

  @Mock
  RestTemplate restTemplate;
  @Mock
  Clock clock;

  AuthService service;

  @BeforeEach
  void beforeEach() {
    service = new AuthService(HOST, restTemplate, clock);
  }

  @Test
  void testAuthorize_callsExternalService() {
    when(restTemplate.getForObject(HOST, Authorization.class))
        .thenReturn(new Authorization("token", 0));

    String token = service.getAuthorizationToken();

    verify(restTemplate).getForObject(HOST, Authorization.class);
    assertEquals("token", token);
  }

  @Test
  void testAuthorize_callsExternalOnceWithinTimeout() {
    when(clock.now()).thenReturn(100L);
    when(restTemplate.getForObject(HOST, Authorization.class))
        .thenReturn(new Authorization("token", 200L));

    service.getAuthorizationToken();
    service.getAuthorizationToken();

    verify(restTemplate, times(1)).getForObject(anyString(), any());
  }

  @Test
  void testAuthorize_callsExternalAgainAfterTimeout() {
    when(clock.now()).thenReturn(100L);
    when(restTemplate.getForObject(HOST, Authorization.class))
        .thenReturn(new Authorization("token", 200L));

    service.getAuthorizationToken();
    when(clock.now()).thenReturn(300L);
    service.getAuthorizationToken();

    verify(restTemplate, times(2)).getForObject(anyString(), any());
  }
}