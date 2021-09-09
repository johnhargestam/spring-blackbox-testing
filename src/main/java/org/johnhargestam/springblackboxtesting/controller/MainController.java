package org.johnhargestam.springblackboxtesting.controller;

import org.johnhargestam.springblackboxtesting.domain.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("main")
public class MainController {

  @GetMapping("resource")
  public Resource getResource() {
    return new Resource("hello");
  }
}
