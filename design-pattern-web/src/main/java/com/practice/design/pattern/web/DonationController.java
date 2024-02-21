package com.practice.design.pattern.web;

import com.practice.design.pattern.entity.Donation;
import com.practice.design.pattern.service.DonationService;
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

  private final DonationService donationService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<Donation> donate(@RequestBody DonateBookWebRequest webRequest) {
    return donationService.donate(webRequest.getMemberId(), webRequest.getBookId(), webRequest.getBookName(), webRequest.getQuantity());
  }

}