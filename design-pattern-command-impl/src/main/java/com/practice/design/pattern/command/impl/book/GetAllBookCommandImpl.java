package com.practice.design.pattern.command.impl.book;

import com.practice.design.pattern.command.book.GetAllBookCommand;
import com.practice.design.pattern.command.model.request.book.GetAllBookCommandRequest;
import com.practice.design.pattern.command.model.response.book.GetAllBookCommandResponse;
import com.practice.design.pattern.common.exception.DataNotFoundException;
import com.practice.design.pattern.entity.Book;
import com.practice.design.pattern.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetAllBookCommandImpl implements GetAllBookCommand {

  private final BookRepository bookRepository;

  @Override
  public Mono<GetAllBookCommandResponse> execute(GetAllBookCommandRequest commandRequest) {
    return bookRepository.findAll()
        .switchIfEmpty(Mono.error(new DataNotFoundException(Book.class, new HashMap<>())))
        .collectList()
        .map(books -> GetAllBookCommandResponse.builder()
            .books(books)
            .build())
        .doOnSuccess(book -> log.info("Successfully get all books"));
  }

}
