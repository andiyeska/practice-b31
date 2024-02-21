package com.practice.design.pattern.command.model.response.borrowing;

import com.practice.design.pattern.entity.Borrowing;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BorrowBookCommandResponse {
  private Borrowing borrowing;
}
