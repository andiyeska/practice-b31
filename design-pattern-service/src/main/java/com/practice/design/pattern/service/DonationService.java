package com.practice.design.pattern.service;

import com.practice.design.pattern.entity.Borrowing;
import com.practice.design.pattern.entity.Donation;
import reactor.core.publisher.Mono;

public interface DonationService {
  Mono<Donation> donate(String memberId, String bookId, String bookName, int quantity);
}
