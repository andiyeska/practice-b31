package com.practice.design.pattern.web.model.borrowing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveBorrowingWebRequest {
  private String memberId;
  private String bookId;
  private int quantity;
}
