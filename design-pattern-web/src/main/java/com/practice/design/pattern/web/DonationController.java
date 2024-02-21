package com.practice.design.pattern.web;

import com.practice.design.pattern.common.exception.DataNotFoundException;
import com.practice.design.pattern.common.exception.DuplicateDataException;
import com.practice.design.pattern.entity.Book;
import com.practice.design.pattern.entity.Donation;
import com.practice.design.pattern.entity.Member;
import com.practice.design.pattern.repository.BookRepository;
import com.practice.design.pattern.repository.DonationRepository;
import com.practice.design.pattern.repository.MemberRepository;
import com.practice.design.pattern.web.model.donation.DonateBookWebRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(DonationController.BASE_PATH)
public class DonationController {
  public static final String BASE_PATH = "/donations";
  public static final String DONATION_ID = "donation_id";
  public static final String DONATION_ID_PATH = "/{" + DONATION_ID + "}";
  private static final String MEMBER_ID = "member id";
  private static final String BOOK_ID = "book id";

  private final MemberRepository memberRepository;
  private final BookRepository bookRepository;
  private final DonationRepository donationRepository;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<Donation> donate(@RequestBody DonateBookWebRequest webRequest) {
    return memberRepository.findById(webRequest.getMemberId())
        .switchIfEmpty(Mono.error(new DataNotFoundException(Member.class, Map.of(MEMBER_ID, webRequest.getMemberId()))))
        .doOnSuccess(member -> log.info("Successfully update member with id: {}", member.getId()))
        .zipWhen(member -> bookRepository.findById(webRequest.getBookId())
            .switchIfEmpty(Mono.error(new DataNotFoundException(Book.class, Map.of(BOOK_ID, webRequest.getBookId()))))
            .doOnSuccess(book -> log.info("Successfully get book with id: {}", book.getId()))
            .onErrorResume(DataNotFoundException.class, ex ->
                bookRepository.findById(webRequest.getBookId())
                    .switchIfEmpty(Mono.just(Book.builder().build()))
                    .filter(book -> Objects.isNull(book.getId()))
                    .switchIfEmpty(Mono.error(new DuplicateDataException(Book.class, webRequest.getBookId())))
                    .map(book -> book.toBuilder()
                        .id(webRequest.getBookId())
                        .name(webRequest.getBookName())
                        .quantity(0)
                        .build())
                    .flatMap(bookRepository::save)
                    .doOnSuccess(book -> log.info("Successfully save book with id: {}", book.getId())))
            .map(book -> book.toBuilder()
                .quantity(book.getQuantity() + webRequest.getQuantity())
                .build())
            .flatMap(bookRepository::save)
            .doOnSuccess(book -> log.info("Successfully add quantity of book with id: {} and updated quantity: {}", book.getId(), book.getQuantity())))
        .flatMap(tuple -> donationRepository.save(Donation.builder()
            .member(tuple.getT1())
            .book(tuple.getT2())
            .quantity(webRequest.getQuantity())
            .date(System.currentTimeMillis())
            .build()));
  }

}