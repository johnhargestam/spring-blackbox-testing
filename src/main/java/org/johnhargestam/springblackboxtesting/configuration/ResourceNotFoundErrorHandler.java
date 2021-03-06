package org.johnhargestam.springblackboxtesting.configuration;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;

class ResourceNotFoundErrorHandler extends DefaultResponseErrorHandler {
  @Override
  public boolean hasError(ClientHttpResponse response) throws IOException {
    HttpStatus statusCode = response.getStatusCode();
    return statusCode.is5xxServerError() || (statusCode.is4xxClientError() && !HttpStatus.NOT_FOUND.equals(statusCode));
  }
}
