package org.johnhargestam.springblackboxtesting.service.external.configuration;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

import static java.nio.charset.StandardCharsets.UTF_8;

public class UnexpectedJsonRootMessageConverter extends MappingJackson2HttpMessageConverter {

  @Override
  public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
    String body = getBody(inputMessage);
    if (defaultObjectMapper.readTree(body).isArray() != defaultObjectMapper.constructType(type).isArrayType()) {
      return null;
    }
    return super.read(type, contextClass, inputMessage);
  }

  private String getBody(HttpInputMessage inputMessage) throws IOException {
    InputStream bodyStream = inputMessage.getBody();
    String body = StreamUtils.copyToString(bodyStream, UTF_8);
    bodyStream.reset();
    return body;
  }
}