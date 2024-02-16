package com.practice.design.pattern.command.impl;

import com.practice.design.pattern.command.SaveBookCommand;
import com.practice.design.pattern.command.model.request.SaveBookCommandRequest;
import com.practice.design.pattern.command.model.response.SaveBookCommandResponse;
import com.practice.design.pattern.common.exception.DuplicateDataException;
import com.practice.design.pattern.entity.Book;
import com.practice.design.pattern.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class SaveBookCommandImpl implements SaveBookCommand {

  private final BookRepository bookRepository;

  @Override
  public Mono<SaveBookCommandResponse> execute(SaveBookCommandRequest commandRequest) {
    return bookRepository.findById(commandRequest.getId())
        .switchIfEmpty(Mono.just(Book.builder().build()))
        .filter(book -> Objects.isNull(book.getId()))
        .switchIfEmpty(Mono.error(new DuplicateDataException(Book.class, commandRequest.getId())))
        .map(book -> book.toBuilder()
            .id(commandRequest.getId())
            .name(commandRequest.getName())
            .quantity(commandRequest.getQuantity())
            .build())
        .flatMap(bookRepository::save)
        .map(book -> SaveBookCommandResponse.builder()
            .id(book.getId())
            .name(book.getName())
            .quantity(book.getQuantity())
            .build())
        .doOnSuccess(newBook -> log.info("Successfully save book with id: {}", newBook.getId()));
  }

}
