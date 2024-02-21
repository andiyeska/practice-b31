package com.practice.design.pattern.command.model.response.book;

import com.practice.design.pattern.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveBookCommandResponse {
  private Book book;
}
