package com.practice.design.pattern.web;

import com.practice.design.pattern.common.enums.BorrowingStatus;
import com.practice.design.pattern.common.exception.BadRequestException;
import com.practice.design.pattern.common.exception.DataNotFoundException;
import com.practice.design.pattern.entity.Book;
import com.practice.design.pattern.entity.Borrowing;
import com.practice.design.pattern.entity.Member;
import com.practice.design.pattern.repository.BookRepository;
import com.practice.design.pattern.repository.BorrowingRepository;
import com.practice.design.pattern.repository.MemberRepository;
import com.practice.design.pattern.web.model.borrowing.SaveBorrowingWebRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

import static com.practice.design.pattern.web.BookController.BOOK_ID;
import static com.practice.design.pattern.web.MemberController.MEMBER_ID;

@Slf4j
@RequiredArgsConstructor
@RestController(BorrowingController.BASE_PATH)
public class BorrowingController {
  public static final String BASE_PATH = "/borrowings";
  public static final String BORROWING_ID = "borrowing_id";
  public static final String BORROWING_ID_PATH = "/{" + BORROWING_ID + "}";
  public static final String RETURN_PATH = "/_return";

  private final MemberRepository memberRepository;
  private final BookRepository bookRepository;
  private final BorrowingRepository borrowingRepository;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<Borrowing> save(@RequestBody SaveBorrowingWebRequest webRequest) {
    return memberRepository.findById(webRequest.getMemberId())
        .switchIfEmpty(Mono.error(new DataNotFoundException(Member.class, Map.of(MEMBER_ID, webRequest.getMemberId()))))
        .flatMap(member -> borrow(member, webRequest))
        .doOnSuccess(borrowing -> log.info("Successfully save borrowing with id: {}", borrowing.getId()));
  }

  private Mono<Borrowing> borrow(Member member, SaveBorrowingWebRequest webRequest) {
    return bookRepository.findById(webRequest.getBookId())
        .switchIfEmpty(Mono.error(new DataNotFoundException(Book.class, Map.of(BOOK_ID, webRequest.getBookId()))))
        .filter(book -> (book.getQuantity() - webRequest.getQuantity()) >= 0)
        .switchIfEmpty(Mono.error(new BadRequestException("reduced quantity is greater than availability")))
        .map(book -> book.toBuilder()
            .quantity(book.getQuantity() - webRequest.getQuantity())
            .build())
        .flatMap(bookRepository::save)
        .map(book -> Borrowing.builder()
            .member(member)
            .book(book)
            .quantity(webRequest.getQuantity())
            .borrowingStatus(BorrowingStatus.BORROWED)
            .build())
        .flatMap(borrowingRepository::save);
  }

  @PostMapping(BORROWING_ID_PATH + RETURN_PATH)
  @ResponseStatus(HttpStatus.OK)
  public Mono<Borrowing> returnBorrowing(@PathVariable(BORROWING_ID) String borrowingId) {
    return borrowingRepository.findById(borrowingId)
        .switchIfEmpty(Mono.error(new DataNotFoundException(Borrowing.class, Map.of(BORROWING_ID, borrowingId))))
        .map(borrowing -> borrowing.toBuilder()
            .borrowingStatus(BorrowingStatus.RETURNED)
            .build())
        .flatMap(borrowingRepository::save)
        .flatMap(this::addBookQuantity);
  }

  private Mono<Borrowing> addBookQuantity(Borrowing borrowing) {
    return bookRepository.findById(borrowing.getBook().getId())
        .switchIfEmpty(Mono.error(new DataNotFoundException(Book.class, Map.of(BOOK_ID, borrowing.getBook().getId()))))
        .map(book -> book.toBuilder()
            .quantity(book.getQuantity() + borrowing.getQuantity())
            .build())
        .map(book -> borrowing);
  }

}
