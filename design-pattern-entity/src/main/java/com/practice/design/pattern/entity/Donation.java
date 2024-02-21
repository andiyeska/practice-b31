package com.practice.design.pattern.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder(toBuilder = true)
@Document(Donation.COLLECTION)
public class Donation {

  public static final String COLLECTION = "donations";

  @Id
  private String id;
  private Member member;
  private Book book;
  private int quantity;
  private long date;

}