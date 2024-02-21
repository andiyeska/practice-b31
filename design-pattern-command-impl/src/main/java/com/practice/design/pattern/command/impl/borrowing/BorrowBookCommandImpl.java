package com.practice.design.pattern.command.impl.borrowing;

import com.practice.design.pattern.command.borrowing.BorrowBookCommand;
import com.practice.design.pattern.command.model.request.borrowing.BorrowBookCommandRequest;
import com.practice.design.pattern.command.model.response.borrowing.BorrowBookCommandResponse;
import com.practice.design.pattern.entity.Borrowing;
import com.practice.design.pattern.repository.BorrowingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class BorrowBookCommandImpl implements BorrowBookCommand {

  private final BorrowingRepository borrowingRepository;

  @Override
  public Mono<BorrowBookCommandResponse> execute(BorrowBookCommandRequest commandRequest) {
    return borrowingRepository.save(Borrowing.builder()
            .book(commandRequest.getBook())
            .member(commandRequest.getMember())
            .build())
        .map(borrowing -> BorrowBookCommandResponse.builder()
            .borrowing(borrowing)
            .build())
        .doOnSuccess(commandResponse -> log.info("Successfully save borrowing with id: {}", commandResponse.getBorrowing().getId()));
  }

}
