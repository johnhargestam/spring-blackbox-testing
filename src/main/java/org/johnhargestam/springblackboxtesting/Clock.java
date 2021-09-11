package org.johnhargestam.springblackboxtesting;

import org.springframework.stereotype.Component;

@Component
public class Clock {
  public long now() {
    return System.currentTimeMillis() / 1000L;
  }
}
