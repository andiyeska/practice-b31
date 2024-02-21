package com.practice.design.pattern.command.impl.borrowing;

import com.practice.design.pattern.command.borrowing.BorrowBookCommand;
import com.practice.design.pattern.command.borrowing.ReturnBookCommand;
import com.practice.design.pattern.command.model.request.borrowing.BorrowBookCommandRequest;
import com.practice.design.pattern.command.model.request.borrowing.ReturnBookCommandRequest;
import com.practice.design.pattern.command.model.response.borrowing.BorrowBookCommandResponse;
import com.practice.design.pattern.command.model.response.borrowing.ReturnBookCommandResponse;
import com.practice.design.pattern.common.enums.BorrowingStatus;
import com.practice.design.pattern.entity.Borrowing;
import com.practice.design.pattern.repository.BorrowingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReturnBookCommandImpl implements ReturnBookCommand {

  private final BorrowingRepository borrowingRepository;

  @Override
  public Mono<ReturnBookCommandResponse> execute(ReturnBookCommandRequest commandRequest) {
    return borrowingRepository.findById(commandRequest.getId())
        .map(borrowing -> borrowing.toBuilder()
            .borrowingStatus(BorrowingStatus.RETURNED)
            .build())
        .flatMap(borrowingRepository::save)
        .map(borrowing -> ReturnBookCommandResponse.builder()
            .borrowing(borrowing)
            .build())
        .doOnSuccess(commandResponse -> log.info("Successfully return borrowing with id: {}", commandResponse.getBorrowing().getId()));
  }

}
