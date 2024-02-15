package com.practice.design.pattern.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder(toBuilder = true)
@Document(Member.COLLECTION)
public class Member {

  public static final String COLLECTION = "members";

  @Id
  private String id;
  private String name;

}
