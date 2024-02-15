package com.practice.design.pattern.entity;

import com.practice.design.pattern.common.enums.BorrowingStatus;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder(toBuilder = true)
@Document(Borrowing.COLLECTION)
public class Borrowing {

  public static final String COLLECTION = "borrowings";

  @Id
  private String id;
  private Member member;
  private Book book;
  private int quantity;
  private BorrowingStatus borrowingStatus;

}
