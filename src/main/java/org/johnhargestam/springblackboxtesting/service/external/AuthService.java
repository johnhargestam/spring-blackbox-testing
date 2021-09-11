package org.johnhargestam.springblackboxtesting.service.external;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.johnhargestam.springblackboxtesting.service.external.response.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class AuthService {

  private final String authHost;
  private final RestTemplate restTemplate;
  private final Observable<Authorization> observable;

  @Autowired
  public AuthService(
      @Value("${external.auth.host}") String host,
      RestTemplate restTemplate
  ) {
    this(host, restTemplate, Schedulers.io());
  }

  protected AuthService(
      String host,
      RestTemplate restTemplate,
      Scheduler scheduler
  ) {
    this.authHost = host;
    this.restTemplate = restTemplate;
    this.observable = Observable.fromCallable(this::getAuthorization)
        .replay(1)
        .refCount(10, TimeUnit.SECONDS, scheduler);
  }

  public String authorize() {
    Authorization authorization = observable.blockingFirst();
    return authorization.token();
  }

  private Authorization getAuthorization() {
    Authorization authorization = restTemplate.getForObject(authHost, Authorization.class);
    return Optional.ofNullable(authorization).orElseThrow();
  }
}
