package com.practice.design.pattern.service.impl;

import com.practice.design.pattern.common.exception.BadRequestException;
import com.practice.design.pattern.common.exception.DataNotFoundException;
import com.practice.design.pattern.common.exception.DuplicateDataException;
import com.practice.design.pattern.entity.Book;
import com.practice.design.pattern.repository.BookRepository;
import com.practice.design.pattern.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

  private static final String BOOK_ID = "book_id";

  private final BookRepository bookRepository;

  @Override
  public Mono<Book> update(Book book) {
    return get(book.getId())
        .map(updatedBook -> updatedBook.toBuilder()
            .name(book.getName())
            .quantity(book.getQuantity())
            .build())
        .flatMap(bookRepository::save)
        .doOnSuccess(updatedBook -> log.info("Successfully update book with id: {}", updatedBook.getId()));
  }

  @Override
  public Mono<Book> get(String id) {
    return bookRepository.findById(id)
        .switchIfEmpty(Mono.error(new DataNotFoundException(Book.class, Map.of(BOOK_ID, id))))
        .doOnSuccess(book -> log.info("Successfully get book with id: {}", book.getId()));
  }

  @Override
  public Mono<List<Book>> getAll() {
    return bookRepository.findAll()
        .switchIfEmpty(Mono.error(new DataNotFoundException(Book.class, new HashMap<>())))
        .collectList()
        .doOnSuccess(book -> log.info("Successfully get all books"));
  }

  @Override
  public Mono<Book> addQuantity(String id, int addedQuantity) {
    return get(id)
        .map(book -> book.toBuilder()
            .quantity(book.getQuantity() + addedQuantity)
            .build())
        .flatMap(bookRepository::save)
        .doOnSuccess(book -> log.info("Successfully add quantity of book with id: {} and updated quantity: {}", book.getId(), book.getQuantity()));
  }

  @Override
  public Mono<Book> reduceQuantity(String id, int reducedQuantity) {
    return get(id)
        .filter(book -> (book.getQuantity() - reducedQuantity) >= 0)
        .switchIfEmpty(Mono.error(new BadRequestException("reduced quantity is greater than availability")))
        .map(book -> book.toBuilder()
            .quantity(book.getQuantity() - reducedQuantity)
            .build())
        .flatMap(bookRepository::save)
        .doOnSuccess(book -> log.info("Successfully reduce quantity of book with id: {} and updated quantity: {}", book.getId(), book.getQuantity()));
  }
}
