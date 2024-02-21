package com.practice.design.pattern.command.impl.book;

import com.practice.design.pattern.command.book.AddQuantityBookCommand;
import com.practice.design.pattern.command.model.request.book.AddQuantityBookCommandRequest;
import com.practice.design.pattern.command.model.response.book.AddQuantityBookCommandResponse;
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
public class AddQuantityBookCommandImpl implements AddQuantityBookCommand {

  private static final String BOOK_ID = "book id";

  private final BookRepository bookRepository;

  @Override
  public Mono<AddQuantityBookCommandResponse> execute(AddQuantityBookCommandRequest commandRequest) {
    return Mono.just(commandRequest.getBook())
        .map(book -> book.toBuilder()
            .quantity(book.getQuantity() + commandRequest.getQuantity())
            .build())
        .flatMap(bookRepository::save)
        .map(book -> AddQuantityBookCommandResponse.builder()
            .book(book)
            .build())
        .doOnSuccess(commandResponse -> log.info("Successfully add quantity of book with id: {} and updated quantity: {}", commandResponse.getBook().getId(), commandResponse.getBook().getQuantity()));
  }

}
