package com.practice.design.pattern.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder(toBuilder = true)
@Document(Book.COLLECTION)
public class Book {

  public static final String COLLECTION = "books";

  @Id
  private String id;
  private String name;
  private int quantity;

}
