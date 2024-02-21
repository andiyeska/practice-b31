package com.practice.design.pattern.command.model.request.book;

import com.practice.design.pattern.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddQuantityBookCommandRequest {
  private Book book;
  private int quantity;
}
