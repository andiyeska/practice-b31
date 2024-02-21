package com.practice.design.pattern.command.model.request.borrowing;

import com.practice.design.pattern.entity.Book;
import com.practice.design.pattern.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReturnBookCommandRequest {
  private String id;
}
