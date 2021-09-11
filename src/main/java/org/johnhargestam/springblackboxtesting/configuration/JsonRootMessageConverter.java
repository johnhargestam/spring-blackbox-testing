package org.johnhargestam.springblackboxtesting.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

public class JsonRootMessageConverter extends MappingJackson2HttpMessageConverter {

  private final ObjectMapper objectMapper;

  public JsonRootMessageConverter() {
    objectMapper = new ObjectMapper();
  }

  @Override
  public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
    InputStream inputStream = inputMessage.getBody();
    String body = StreamUtils.copyToString(inputStream, Charset.defaultCharset());
    inputStream.reset();

    if (objectMapper.readTree(body).isArray() != objectMapper.constructType(type).isArrayType()) {
      return null;
    }
    return super.read(type, contextClass, inputMessage);
  }
}