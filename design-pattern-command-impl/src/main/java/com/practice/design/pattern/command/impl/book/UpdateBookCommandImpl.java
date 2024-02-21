package com.practice.design.pattern.command.impl.book;

import com.practice.design.pattern.command.book.UpdateBookCommand;
import com.practice.design.pattern.command.model.request.book.UpdateBookCommandRequest;
import com.practice.design.pattern.command.model.response.book.UpdateBookCommandResponse;
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
public class UpdateBookCommandImpl implements UpdateBookCommand {

  private static final String BOOK_ID = "book id";

  private final BookRepository bookRepository;

  @Override
  public Mono<UpdateBookCommandResponse> execute(UpdateBookCommandRequest commandRequest) {
    return bookRepository.findById(commandRequest.getId())
        .switchIfEmpty(Mono.error(new DataNotFoundException(Book.class, Map.of(BOOK_ID, commandRequest.getId()))))
        .map(book -> book.toBuilder()
            .name(commandRequest.getName())
            .quantity(commandRequest.getQuantity())
            .build())
        .flatMap(bookRepository::save)
        .map(book -> UpdateBookCommandResponse.builder()
            .book(book)
            .build())
        .doOnSuccess(commandResponse -> log.info("Successfully update book with id: {}", commandResponse.getBook().getId()));
  }

}
