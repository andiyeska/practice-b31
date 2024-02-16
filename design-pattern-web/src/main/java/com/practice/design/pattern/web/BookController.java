package com.practice.design.pattern.web;

import com.practice.design.pattern.command.SaveBookCommand;
import com.practice.design.pattern.command.impl.executor.CommandExecutor;
import com.practice.design.pattern.command.model.request.SaveBookCommandRequest;
import com.practice.design.pattern.command.model.response.SaveBookCommandResponse;
import com.practice.design.pattern.entity.Book;
import com.practice.design.pattern.service.BookService;
import com.practice.design.pattern.web.model.book.SaveBookWebRequest;
import com.practice.design.pattern.web.model.book.UpdateBookWebRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(BookController.BASE_PATH)
public class BookController {

  public static final String BASE_PATH = "/books";
  public static final String BOOK_ID = "book_id";
  public static final String BOOK_ID_PATH = "/{" + BOOK_ID + "}";
  private static final String ADD_QUANTITY_PATH = "/_add-quantity";
  private static final String REDUCE_QUANTITY_PATH = "/_reduce-quantity";

  private final CommandExecutor commandExecutor;
  private final BookService bookService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<SaveBookCommandResponse> save(@RequestBody SaveBookWebRequest webRequest) {
    return commandExecutor.execute(SaveBookCommand.class,
            SaveBookCommandRequest.builder()
                .id(webRequest.getId())
                .name(webRequest.getName())
                .quantity(webRequest.getQuantity())
                .build());
  }

  @PutMapping(BOOK_ID_PATH)
  @ResponseStatus(HttpStatus.OK)
  public Mono<Book> update(@PathVariable(BOOK_ID) String bookId, @RequestBody UpdateBookWebRequest webRequest) {
    return bookService.update(
        Book.builder()
            .id(bookId)
            .name(webRequest.getName())
            .quantity(webRequest.getQuantity())
            .build());
  }

  @GetMapping(BOOK_ID_PATH)
  @ResponseStatus(HttpStatus.OK)
  public Mono<Book> get(@PathVariable(BOOK_ID) String bookId) {
    return bookService.get(bookId);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Mono<List<Book>> getAll() {
    return bookService.getAll();
  }

  @PutMapping(BOOK_ID_PATH + ADD_QUANTITY_PATH)
  @ResponseStatus(HttpStatus.OK)
  public Mono<Book> addQuantity(@PathVariable(BOOK_ID) String bookId, @RequestBody int quantity) {
    return bookService.addQuantity(bookId, quantity);
  }

  @PutMapping(BOOK_ID_PATH + REDUCE_QUANTITY_PATH)
  @ResponseStatus(HttpStatus.OK)
  public Mono<Book> reduceQuantity(@PathVariable(BOOK_ID) String bookId, @RequestBody int quantity) {
    return bookService.reduceQuantity(bookId, quantity);
  }

}
