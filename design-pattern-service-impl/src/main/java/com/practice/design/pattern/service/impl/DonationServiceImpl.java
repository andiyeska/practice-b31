package com.practice.design.pattern.service.impl;

import com.practice.design.pattern.common.exception.DataNotFoundException;
import com.practice.design.pattern.entity.Book;
import com.practice.design.pattern.entity.Donation;
import com.practice.design.pattern.repository.DonationRepository;
import com.practice.design.pattern.service.BookService;
import com.practice.design.pattern.service.DonationService;
import com.practice.design.pattern.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class DonationServiceImpl implements DonationService {

  private final MemberService memberService;
  private final BookService bookService;

  private final DonationRepository donationRepository;

  @Override
  public Mono<Donation> donate(String memberId, String bookId, String bookName, int quantity) {
    return memberService.get(memberId)
        .zipWhen(member -> bookService.get(bookId)
            .onErrorResume(DataNotFoundException.class, ex ->
                bookService.save(Book.builder()
                    .id(bookId)
                    .name(bookName)
                    .build()))
            .flatMap(book -> bookService.addQuantity(bookId, quantity)))
        .flatMap(tuple -> donationRepository.save(Donation.builder()
            .member(tuple.getT1())
            .book(tuple.getT2())
            .quantity(quantity)
            .date(System.currentTimeMillis())
            .build()));
  }
}
