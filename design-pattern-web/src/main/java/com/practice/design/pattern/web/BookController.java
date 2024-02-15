package com.practice.design.pattern.web;

import com.practice.design.pattern.common.exception.BadRequestException;
import com.practice.design.pattern.entity.Book;
import com.practice.design.pattern.common.exception.DataNotFoundException;
import com.practice.design.pattern.common.exception.DuplicateDataException;
import com.practice.design.pattern.repository.BookRepository;
import com.practice.design.pattern.service.model.book.SaveBookWebRequest;
import com.practice.design.pattern.web.model.book.UpdateBookWebRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@RestController(BookController.BASE_PATH)
public class BookController {

  public static final String BASE_PATH = "/books";
  public static final String BOOK_ID = "book_id";
  public static final String BOOK_ID_PATH = "/{" + BOOK_ID + "}";
  private static final String ADD_QUANTITY_PATH = "/_add-quantity";
  private static final String REDUCE_QUANTITY_PATH = "/_reduce-quantity";

  private final BookRepository bookRepository;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<Book> save(@RequestBody SaveBookWebRequest webRequest) {
    return bookRepository.findById(webRequest.getId())
        .switchIfEmpty(Mono.just(Book.builder().build()))
        .filter(book -> Objects.isNull(book.getId()))
        .switchIfEmpty(Mono.error(new DuplicateDataException(Book.class, webRequest.getId())))
        .map(book -> book.toBuilder()
            .id(webRequest.getId())
            .name(webRequest.getName())
            .quantity(webRequest.getQuantity())
            .build())
        .flatMap(bookRepository::save)
        .doOnSuccess(book -> log.info("Successfully save book with id: {}", book.getId()));
  }

  @PutMapping(BOOK_ID_PATH)
  @ResponseStatus(HttpStatus.OK)
  public Mono<Book> update(@PathVariable(BOOK_ID) String bookId, @RequestBody UpdateBookWebRequest webRequest) {
    return bookRepository.findById(bookId)
        .switchIfEmpty(Mono.error(new DataNotFoundException(Book.class, Map.of(BOOK_ID, bookId))))
        .map(book -> book.toBuilder()
            .name(webRequest.getName())
            .quantity(webRequest.getQuantity())
            .build())
        .flatMap(bookRepository::save)
        .doOnSuccess(book -> log.info("Successfully update book with id: {}", book.getId()));
  }

  @GetMapping(BOOK_ID_PATH)
  @ResponseStatus(HttpStatus.OK)
  public Mono<Book> get(@PathVariable(BOOK_ID) String bookId) {
    return bookRepository.findById(bookId)
        .switchIfEmpty(Mono.error(new DataNotFoundException(Book.class, Map.of(BOOK_ID, bookId))))
        .doOnSuccess(book -> log.info("Successfully get book with id: {}", book.getId()));
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Flux<Book> getAll() {
    return bookRepository.findAll()
        .switchIfEmpty(Mono.error(new DataNotFoundException(Book.class, new HashMap<>())))
        .doOnNext(book -> log.info("Successfully get all books"));
  }

  @PutMapping(BOOK_ID_PATH + ADD_QUANTITY_PATH)
  @ResponseStatus(HttpStatus.OK)
  public Mono<Book> addQuantity(@PathVariable(BOOK_ID) String bookId, @RequestBody int quantity) {
    return bookRepository.findById(bookId)
        .switchIfEmpty(Mono.error(new DataNotFoundException(Book.class, Map.of(BOOK_ID, bookId))))
        .map(book -> book.toBuilder()
            .quantity(book.getQuantity() + quantity)
            .build())
        .flatMap(bookRepository::save)
        .doOnSuccess(book -> log.info("Successfully add quantity of book with id: {} and updated quantity: {}", book.getId(), book.getQuantity()));
  }

  @PutMapping(BOOK_ID_PATH + REDUCE_QUANTITY_PATH)
  @ResponseStatus(HttpStatus.OK)
  public Mono<Book> reduceQuantity(@PathVariable(BOOK_ID) String bookId, @RequestBody int quantity) {
    return bookRepository.findById(bookId)
        .switchIfEmpty(Mono.error(new DataNotFoundException(Book.class, Map.of(BOOK_ID, bookId))))
        .filter(book -> (book.getQuantity() - quantity) >= 0)
        .switchIfEmpty(Mono.error(new BadRequestException("reduced quantity is greater than availability")))
        .map(book -> book.toBuilder()
            .quantity(book.getQuantity() - quantity)
            .build())
        .flatMap(bookRepository::save)
        .doOnSuccess(book -> log.info("Successfully reduce quantity of book with id: {} and updated quantity: {}", book.getId(), book.getQuantity()));
  }

}
