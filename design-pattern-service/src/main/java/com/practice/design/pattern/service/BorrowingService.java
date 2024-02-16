package com.practice.design.pattern.service;

import com.practice.design.pattern.entity.Borrowing;
import reactor.core.publisher.Mono;

public interface BorrowingService {
  Mono<Borrowing> save(String memberId, String bookId, int quantity);
  Mono<Borrowing> returnBorrowing(String id);
}
