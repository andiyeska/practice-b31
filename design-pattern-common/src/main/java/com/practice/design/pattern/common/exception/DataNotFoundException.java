package com.practice.design.pattern.common.exception;

import lombok.Data;

import java.util.Map;

@Data
public class DataNotFoundException extends Exception {

  private Class data;
  private Map<String, String> fieldValue;

  public DataNotFoundException(Class data, Map<String, String> fieldValue) {
    this.data = data;
    this.fieldValue = fieldValue;
  }
}
