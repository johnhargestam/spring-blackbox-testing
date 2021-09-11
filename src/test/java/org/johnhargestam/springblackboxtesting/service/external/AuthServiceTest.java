package org.johnhargestam.springblackboxtesting.service.external;

import io.reactivex.rxjava3.schedulers.TestScheduler;
import org.johnhargestam.springblackboxtesting.service.external.response.Authorization;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class AuthServiceTest {
  final String HOST = "host";

  @Mock
  RestTemplate restTemplate;

  AuthService service;
  TestScheduler scheduler;

  @BeforeEach
  void beforeEach() {
    scheduler = new TestScheduler();
    service = new AuthService(HOST, restTemplate, scheduler);

    when(restTemplate.getForObject(HOST, Authorization.class))
        .thenReturn(new Authorization("token"));
  }

  @Test
  void testAuthorize_callsExternalService() {
    String token = service.authorize();

    verify(restTemplate).getForObject(HOST, Authorization.class);
    assertEquals("token", token);
  }

  @Test
  void testAuthorize_callsExternalOnceUntilTimeout() {
    service.authorize();
    service.authorize();

    verify(restTemplate, times(1)).getForObject(anyString(), any());
  }

  @Test
  void testAuthorize_callsExternalAgainAfterTimeout() {
    service.authorize();
    scheduler.advanceTimeBy(10, TimeUnit.SECONDS);
    service.authorize();

    verify(restTemplate, times(2)).getForObject(anyString(), any());
  }
}