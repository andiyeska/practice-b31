package com.practice.design.pattern.web;

import com.practice.design.pattern.command.book.AddQuantityBookCommand;
import com.practice.design.pattern.command.book.GetBookCommand;
import com.practice.design.pattern.command.book.ReduceQuantityBookCommand;
import com.practice.design.pattern.command.borrowing.BorrowBookCommand;
import com.practice.design.pattern.command.borrowing.ReturnBookCommand;
import com.practice.design.pattern.command.impl.executor.CommandExecutor;
import com.practice.design.pattern.command.impl.member.GetMemberCommandImpl;
import com.practice.design.pattern.command.model.request.book.AddQuantityBookCommandRequest;
import com.practice.design.pattern.command.model.request.book.GetBookCommandRequest;
import com.practice.design.pattern.command.model.request.book.ReduceQuantityBookCommandRequest;
import com.practice.design.pattern.command.model.request.borrowing.BorrowBookCommandRequest;
import com.practice.design.pattern.command.model.request.borrowing.ReturnBookCommandRequest;
import com.practice.design.pattern.command.model.request.member.GetMemberCommandRequest;
import com.practice.design.pattern.command.model.response.book.GetBookCommandResponse;
import com.practice.design.pattern.command.model.response.borrowing.BorrowBookCommandResponse;
import com.practice.design.pattern.command.model.response.borrowing.ReturnBookCommandResponse;
import com.practice.design.pattern.entity.Borrowing;
import com.practice.design.pattern.web.model.borrowing.SaveBorrowingWebRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(BorrowingController.BASE_PATH)
public class BorrowingController {
  public static final String BASE_PATH = "/borrowings";
  public static final String BORROWING_ID = "borrowing_id";
  public static final String BORROWING_ID_PATH = "/{" + BORROWING_ID + "}";
  public static final String RETURN_PATH = "/_return";

  private final CommandExecutor commandExecutor;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<Borrowing> borrow(@RequestBody SaveBorrowingWebRequest webRequest) {
    return commandExecutor.execute(GetMemberCommandImpl.class, GetMemberCommandRequest.builder()
            .id(webRequest.getMemberId())
            .build())
        .zipWhen(commandResponse -> commandExecutor.execute(GetBookCommand.class, GetBookCommandRequest.builder()
                .id(webRequest.getBookId())
                .build())
            .map(GetBookCommandResponse::getBook)
            .flatMap(book -> commandExecutor.execute(ReduceQuantityBookCommand.class, ReduceQuantityBookCommandRequest.builder()
                .book(book)
                .quantity(webRequest.getQuantity())
                .build())))
        .flatMap(tuple -> commandExecutor.execute(BorrowBookCommand.class, BorrowBookCommandRequest.builder()
            .book(tuple.getT2().getBook())
            .member(tuple.getT1().getMember())
            .build()))
        .map(BorrowBookCommandResponse::getBorrowing);
  }

  @PostMapping(BORROWING_ID_PATH + RETURN_PATH)
  @ResponseStatus(HttpStatus.OK)
  public Mono<Borrowing> returnBorrowing(@PathVariable(BORROWING_ID) String borrowingId) {
    return commandExecutor.execute(ReturnBookCommand.class, ReturnBookCommandRequest.builder()
            .id(borrowingId)
            .build())
        .map(ReturnBookCommandResponse::getBorrowing)
        .doOnNext(borrowing -> commandExecutor.execute(AddQuantityBookCommand.class, AddQuantityBookCommandRequest.builder()
            .book(borrowing.getBook())
            .quantity(borrowing.getQuantity())
            .build()));
  }

}
