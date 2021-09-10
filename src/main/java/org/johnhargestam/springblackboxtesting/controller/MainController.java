package org.johnhargestam.springblackboxtesting.controller;

import org.johnhargestam.springblackboxtesting.domain.Resource;
import org.johnhargestam.springblackboxtesting.service.external.ExternalService;
import org.johnhargestam.springblackboxtesting.service.external.response.ExternalResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("main")
public class MainController {

  private final ExternalService service;

  public MainController(ExternalService service) {
    this.service = service;
  }

  @GetMapping("resource")
  public List<Resource> getResources() {
    return service.getResources().stream()
        .map(ExternalResource::property)
        .map(Resource::new)
        .collect(Collectors.toList());
  }
}
