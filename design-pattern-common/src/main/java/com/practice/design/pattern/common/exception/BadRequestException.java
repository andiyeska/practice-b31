package com.practice.design.pattern.common.exception;

import lombok.Data;

import java.util.Map;

@Data
public class BadRequestException extends Exception {

  public BadRequestException(String message) {
    super(message);
  }
}
