package com.practice.design.pattern.web;

import com.practice.design.pattern.command.book.AddQuantityBookCommand;
import com.practice.design.pattern.command.book.GetBookCommand;
import com.practice.design.pattern.command.book.SaveBookCommand;
import com.practice.design.pattern.command.donation.DonateBookCommand;
import com.practice.design.pattern.command.impl.executor.CommandExecutor;
import com.practice.design.pattern.command.member.GetMemberCommand;
import com.practice.design.pattern.command.model.request.book.AddQuantityBookCommandRequest;
import com.practice.design.pattern.command.model.request.book.GetBookCommandRequest;
import com.practice.design.pattern.command.model.request.book.SaveBookCommandRequest;
import com.practice.design.pattern.command.model.request.donation.DonateBookCommandRequest;
import com.practice.design.pattern.command.model.request.member.GetMemberCommandRequest;
import com.practice.design.pattern.command.model.response.book.AddQuantityBookCommandResponse;
import com.practice.design.pattern.command.model.response.book.GetBookCommandResponse;
import com.practice.design.pattern.command.model.response.book.SaveBookCommandResponse;
import com.practice.design.pattern.command.model.response.donation.DonateBookCommandResponse;
import com.practice.design.pattern.command.model.response.member.GetMemberCommandResponse;
import com.practice.design.pattern.common.exception.DataNotFoundException;
import com.practice.design.pattern.entity.Book;
import com.practice.design.pattern.entity.Donation;
import com.practice.design.pattern.entity.Member;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(DonationController.BASE_PATH)
public class DonationController {
  public static final String BASE_PATH = "/donations";
  public static final String DONATION_ID = "donation_id";
  public static final String DONATION_ID_PATH = "/{" + DONATION_ID + "}";

  private final CommandExecutor commandExecutor;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<Donation> donate(@RequestBody DonateBookWebRequest webRequest) {
    return commandExecutor.execute(GetMemberCommand.class, GetMemberCommandRequest.builder()
            .id(webRequest.getMemberId())
            .build())
        .map(GetMemberCommandResponse::getMember)
        .zipWhen(member -> commandExecutor.execute(GetBookCommand.class, GetBookCommandRequest.builder()
                .id(webRequest.getBookId())
                .build())
            .map(GetBookCommandResponse::getBook)
            .onErrorResume(DataNotFoundException.class, ex ->
                commandExecutor.execute(SaveBookCommand.class,
                        SaveBookCommandRequest.builder()
                            .id(webRequest.getBookId())
                            .name(webRequest.getBookName())
                            .build())
                    .map(SaveBookCommandResponse::getBook))
            .flatMap(book -> commandExecutor.execute(AddQuantityBookCommand.class, AddQuantityBookCommandRequest.builder()
                .book(book)
                .quantity(webRequest.getQuantity())
                .build()))
            .map(AddQuantityBookCommandResponse::getBook))
        .flatMap(tuple -> commandExecutor.execute(DonateBookCommand.class, DonateBookCommandRequest.builder()
            .member(tuple.getT1())
            .book(tuple.getT2())
            .quantity(webRequest.getQuantity())
            .build()))
        .map(DonateBookCommandResponse::getDonation);
  }

}
