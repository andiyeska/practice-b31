package com.practice.design.pattern.web.exception;

import com.practice.design.pattern.common.exception.DuplicateDataException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(DuplicateDataException.class)
  public String duplicateDataException(DuplicateDataException ex) {
    String message = String.format("duplicate data of: %s with id: %s", ex.getData().getSimpleName(), ex.getId());
    log.error(message, ex);
    return message;
  }

}
