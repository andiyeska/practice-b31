package com.practice.design.pattern.command.impl.book;

import com.practice.design.pattern.command.book.GetBookCommand;
import com.practice.design.pattern.command.model.request.book.GetBookCommandRequest;
import com.practice.design.pattern.command.model.response.book.GetBookCommandResponse;
import com.practice.design.pattern.common.exception.DataNotFoundException;
import com.practice.design.pattern.entity.Book;
import com.practice.design.pattern.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetBookCommandImpl implements GetBookCommand {

  private static final String BOOK_ID = "book id";

  private final BookRepository bookRepository;

  @Override
  public Mono<GetBookCommandResponse> execute(GetBookCommandRequest commandRequest) {
    return bookRepository.findById(commandRequest.getId())
        .switchIfEmpty(Mono.error(new DataNotFoundException(Book.class, Map.of(BOOK_ID, commandRequest.getId()))))
        .map(book -> GetBookCommandResponse.builder()
            .book(book)
            .build())
        .doOnSuccess(commandResponse -> log.info("Successfully get book with id: {}", commandResponse.getBook().getId()));
  }

}
