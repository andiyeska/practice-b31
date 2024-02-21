package com.practice.design.pattern.command.impl.donation;

import com.practice.design.pattern.command.donation.DonateBookCommand;
import com.practice.design.pattern.command.model.request.donation.DonateBookCommandRequest;
import com.practice.design.pattern.command.model.response.donation.DonateBookCommandResponse;
import com.practice.design.pattern.entity.Donation;
import com.practice.design.pattern.repository.DonationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class DonateBookCommandImpl implements DonateBookCommand {

  private final DonationRepository donationRepository;

  @Override
  public Mono<DonateBookCommandResponse> execute(DonateBookCommandRequest commandRequest) {
    return donationRepository.save(Donation.builder()
            .member(commandRequest.getMember())
            .book(commandRequest.getBook())
            .quantity(commandRequest.getQuantity())
            .date(System.currentTimeMillis())
            .build())
        .map(donation -> DonateBookCommandResponse.builder()
            .donation(donation)
            .build())
        .doOnSuccess(commandResponse -> log.info("Successfully save donation with id: {}", commandResponse.getDonation().getId()));
  }

}
