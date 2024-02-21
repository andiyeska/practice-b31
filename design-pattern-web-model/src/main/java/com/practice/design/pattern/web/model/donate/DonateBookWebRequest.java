package com.practice.design.pattern.web.model.donation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DonateBookWebRequest {
  private String memberId;
  private String bookId;
  private String bookName;
  private int quantity;
}