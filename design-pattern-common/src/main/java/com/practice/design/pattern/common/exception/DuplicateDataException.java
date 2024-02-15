package com.practice.design.pattern.common.exception;

import lombok.Data;

@Data
public class DuplicateDataException extends Exception {

  private Class data;
  private String id;

  public DuplicateDataException(Class data, String id) {
    this.data = data;
    this.id = id;
  }
}
