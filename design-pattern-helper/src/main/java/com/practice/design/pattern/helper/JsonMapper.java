package com.practice.design.pattern.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JsonMapper {

  private final ObjectMapper objectMapper;

  public <T> T fromJson(String json, Class<T> clazz) {
    try {
      objectMapper.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);
      return objectMapper.readValue(json, clazz);
    } catch (JsonProcessingException e) {
      log.error("Error when convert objectsJson to Objects", e);
      return null;
    }
  }

  public String toJson(Object object) {
    try {
      return objectMapper.writeValueAsString(object);
    } catch (JsonProcessingException ex) {
      throw new RuntimeException("Failed convert to JSON string", ex);
    }
  }

}
