package com.practice.design.pattern.web;

import com.practice.design.pattern.entity.Borrowing;
import com.practice.design.pattern.service.BorrowingService;
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

  private final BorrowingService borrowingService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<Borrowing> save(@RequestBody SaveBorrowingWebRequest webRequest) {
    return borrowingService.save(webRequest.getMemberId(), webRequest.getBookId(), webRequest.getQuantity());
  }

  @PostMapping(BORROWING_ID_PATH + RETURN_PATH)
  @ResponseStatus(HttpStatus.OK)
  public Mono<Borrowing> returnBorrowing(@PathVariable(BORROWING_ID) String borrowingId) {
    return borrowingService.returnBorrowing(borrowingId);
  }

}
