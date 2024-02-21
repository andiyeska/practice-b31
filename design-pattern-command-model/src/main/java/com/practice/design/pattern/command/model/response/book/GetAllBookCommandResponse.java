package com.practice.design.pattern.command.model.response.book;

import com.practice.design.pattern.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAllBookCommandResponse {
  List<Book> books;
}
