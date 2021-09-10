package org.johnhargestam.springblackboxtesting.controller;

import org.johnhargestam.springblackboxtesting.domain.Resource;
import org.johnhargestam.springblackboxtesting.service.external.ExternalService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("main")
public class MainController {

  private final ExternalService service;

  public MainController(ExternalService service) {
    this.service = service;
  }

  @GetMapping("resource")
  public Resource getResource() {
    String property = service.getResource();
    return new Resource(property);
  }
}
