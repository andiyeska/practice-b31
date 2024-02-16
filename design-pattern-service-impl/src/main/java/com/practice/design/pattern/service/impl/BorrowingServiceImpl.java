package com.practice.design.pattern.service.impl;

import com.practice.design.pattern.common.enums.BorrowingStatus;
import com.practice.design.pattern.common.exception.DataNotFoundException;
import com.practice.design.pattern.entity.Book;
import com.practice.design.pattern.entity.Borrowing;
import com.practice.design.pattern.entity.Member;
import com.practice.design.pattern.repository.BorrowingRepository;
import com.practice.design.pattern.service.BookService;
import com.practice.design.pattern.service.BorrowingService;
import com.practice.design.pattern.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class BorrowingServiceImpl implements BorrowingService {

  private static final String BORROWING_ID = "borrowing_id";

  private final MemberService memberService;
  private final BookService bookService;

  private final BorrowingRepository borrowingRepository;

  @Override
  public Mono<Borrowing> save(String memberId, String bookId, int quantity) {
    return memberService.get(memberId)
        .flatMap(member -> save(member, bookId, quantity))
        .doOnSuccess(borrowing -> log.info("Successfully save borrowing with id: {}", borrowing.getId()));
  }

  private Mono<Borrowing> save(Member member, String bookId, int quantity) {
    return bookService.reduceQuantity(bookId, quantity)
        .map(book -> Borrowing.builder()
            .member(member)
            .book(book)
            .quantity(quantity)
            .borrowingStatus(BorrowingStatus.BORROWED)
            .build())
        .flatMap(borrowingRepository::save);
  }

  @Override
  public Mono<Borrowing> returnBorrowing(String id) {
    return borrowingRepository.findById(id)
        .switchIfEmpty(Mono.error(new DataNotFoundException(Borrowing.class, Map.of(BORROWING_ID, id))))
        .map(borrowing -> borrowing.toBuilder()
            .borrowingStatus(BorrowingStatus.RETURNED)
            .build())
        .flatMap(borrowingRepository::save)
        .flatMap(this::addBookQuantity);
  }

  private Mono<Borrowing> addBookQuantity(Borrowing borrowing) {
    return bookService.addQuantity(borrowing.getBook().getId(), borrowing.getQuantity())
        .map(book -> borrowing);
  }

}
