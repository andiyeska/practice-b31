package com.practice.design.pattern.web;

import com.practice.design.pattern.command.book.AddQuantityBookCommand;
import com.practice.design.pattern.command.book.GetAllBookCommand;
import com.practice.design.pattern.command.book.GetBookCommand;
import com.practice.design.pattern.command.book.ReduceQuantityBookCommand;
import com.practice.design.pattern.command.book.SaveBookCommand;
import com.practice.design.pattern.command.book.UpdateBookCommand;
import com.practice.design.pattern.command.impl.executor.CommandExecutor;
import com.practice.design.pattern.command.model.request.book.AddQuantityBookCommandRequest;
import com.practice.design.pattern.command.model.request.book.GetAllBookCommandRequest;
import com.practice.design.pattern.command.model.request.book.GetBookCommandRequest;
import com.practice.design.pattern.command.model.request.book.ReduceQuantityBookCommandRequest;
import com.practice.design.pattern.command.model.request.book.SaveBookCommandRequest;
import com.practice.design.pattern.command.model.request.book.UpdateBookCommandRequest;
import com.practice.design.pattern.command.model.response.book.AddQuantityBookCommandResponse;
import com.practice.design.pattern.command.model.response.book.GetAllBookCommandResponse;
import com.practice.design.pattern.command.model.response.book.GetBookCommandResponse;
import com.practice.design.pattern.command.model.response.book.ReduceQuantityBookCommandResponse;
import com.practice.design.pattern.command.model.response.book.SaveBookCommandResponse;
import com.practice.design.pattern.command.model.response.book.UpdateBookCommandResponse;
import com.practice.design.pattern.entity.Book;
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

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<Book> save(@RequestBody SaveBookWebRequest webRequest) {
    return commandExecutor.execute(SaveBookCommand.class, SaveBookCommandRequest.builder()
            .id(webRequest.getId())
            .name(webRequest.getName())
            .quantity(webRequest.getQuantity())
            .build())
        .map(SaveBookCommandResponse::getBook);
  }

  @PutMapping(BOOK_ID_PATH)
  @ResponseStatus(HttpStatus.OK)
  public Mono<Book> update(@PathVariable(BOOK_ID) String bookId, @RequestBody UpdateBookWebRequest webRequest) {
    return commandExecutor.execute(UpdateBookCommand.class, UpdateBookCommandRequest.builder()
            .id(bookId)
            .name(webRequest.getName())
            .quantity(webRequest.getQuantity())
            .build())
        .map(UpdateBookCommandResponse::getBook);
  }

  @GetMapping(BOOK_ID_PATH)
  @ResponseStatus(HttpStatus.OK)
  public Mono<Book> get(@PathVariable(BOOK_ID) String bookId) {
    return commandExecutor.execute(GetBookCommand.class, GetBookCommandRequest.builder()
            .id(bookId)
            .build())
        .map(GetBookCommandResponse::getBook);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Mono<List<Book>> getAll() {
    return commandExecutor.execute(GetAllBookCommand.class, GetAllBookCommandRequest.builder()
            .build())
        .map(GetAllBookCommandResponse::getBooks);
  }

  @PutMapping(BOOK_ID_PATH + ADD_QUANTITY_PATH)
  @ResponseStatus(HttpStatus.OK)
  public Mono<Book> addQuantity(@PathVariable(BOOK_ID) String bookId, @RequestBody int quantity) {
    return commandExecutor.execute(GetBookCommand.class, GetBookCommandRequest.builder()
            .id(bookId)
        .build())
        .map(GetBookCommandResponse::getBook)
        .flatMap(book -> commandExecutor.execute(AddQuantityBookCommand.class,AddQuantityBookCommandRequest.builder()
            .book(book)
            .quantity(quantity)
            .build()))
        .map(AddQuantityBookCommandResponse::getBook);
  }

  @PutMapping(BOOK_ID_PATH + REDUCE_QUANTITY_PATH)
  @ResponseStatus(HttpStatus.OK)
  public Mono<Book> reduceQuantity(@PathVariable(BOOK_ID) String bookId, @RequestBody int quantity) {
    return commandExecutor.execute(GetBookCommand.class, GetBookCommandRequest.builder()
            .id(bookId)
            .build())
        .map(GetBookCommandResponse::getBook)
        .flatMap(book -> commandExecutor.execute(ReduceQuantityBookCommand.class, ReduceQuantityBookCommandRequest.builder()
            .book(book)
            .quantity(quantity)
            .build())
        .map(ReduceQuantityBookCommandResponse::getBook));
  }

}
