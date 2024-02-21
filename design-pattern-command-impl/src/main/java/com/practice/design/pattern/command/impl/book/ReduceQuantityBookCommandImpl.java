package com.practice.design.pattern.command.impl.book;

import com.practice.design.pattern.command.book.ReduceQuantityBookCommand;
import com.practice.design.pattern.command.model.request.book.ReduceQuantityBookCommandRequest;
import com.practice.design.pattern.command.model.response.book.ReduceQuantityBookCommandResponse;
import com.practice.design.pattern.common.exception.BadRequestException;
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
public class ReduceQuantityBookCommandImpl implements ReduceQuantityBookCommand {

  private static final String BOOK_ID = "book id";

  private final BookRepository bookRepository;

  @Override
  public Mono<ReduceQuantityBookCommandResponse> execute(ReduceQuantityBookCommandRequest commandRequest) {
    return Mono.just(commandRequest.getBook())
        .filter(book -> (book.getQuantity() - commandRequest.getQuantity()) >= 0)
        .switchIfEmpty(Mono.error(new BadRequestException("reduced quantity is greater than availability")))
        .map(book -> book.toBuilder()
            .quantity(book.getQuantity() - commandRequest.getQuantity())
            .build())
        .flatMap(bookRepository::save)
        .map(book -> ReduceQuantityBookCommandResponse.builder()
            .book(book)
            .build())
        .doOnSuccess(commandResponse -> log.info("Successfully reduce quantity of book with id: {} and updated quantity: {}", commandResponse.getBook().getId(), commandResponse.getBook().getQuantity()));
  }

}
